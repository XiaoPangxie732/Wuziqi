package cn.maxpixel.mods.wuziqi.block.entity;

import cn.maxpixel.mods.wuziqi.annotations.UsedOn;
import cn.maxpixel.mods.wuziqi.board.Board;
import cn.maxpixel.mods.wuziqi.board.PieceType;
import cn.maxpixel.mods.wuziqi.network.Network;
import cn.maxpixel.mods.wuziqi.network.clientbound.ClientboundUpdatePlayersPacket;
import cn.maxpixel.mods.wuziqi.registry.BlockEntityRegistry;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class BoardBlockEntity extends BlockEntity {
    public static final String BOARD_KEY = "Board";
    public static final String MATCHING_KEY = "Matching";
    public static final String TURN_KEY = "Turn";
    public static final String WHITE_KEY = "White";
    public static final String BLACK_KEY = "Black";
    public static final String CLEAR_KEY = "Clear";
    private static final Component WHITE_WIN = Component.translatable(I18nUtil.make("block_entity", "board.win.white"));
    private static final Component BLACK_WIN = Component.translatable(I18nUtil.make("block_entity", "board.win.black"));
    private final Set<ServerPlayer> joinedPlayers = new ObjectOpenHashSet<>();
    private @Nullable Board board;

    // These are only used at runtime
    private transient @UsedOn(UsedOn.Side.SERVER) boolean matchJustEnded;
    private transient boolean matching;
    private transient @Nullable PieceType turn;
    private transient @Nullable Set<Player> white;
    private transient @Nullable Set<Player> black;

    public BoardBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BOARD.get(), pos, state);
    }

    public Board getBoard() {
        if (board == null) this.board = new Board(this::setChanged);
        return board;
    }

    public boolean isMatching() {
        return matching;
    }

    public void setMatching(boolean matching) {
        if (this.matching == matching) return;
        this.matching = matching;
        setChanged();
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void addPlayer(ServerPlayer player) {
        joinedPlayers.add(player);
        syncJoinedPlayers();
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void removePlayer(ServerPlayer player) {
        joinedPlayers.remove(player);
        syncJoinedPlayers();
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void syncJoinedPlayers() {
        var pos = getBlockPos().getCenter();
        Network.CHANNEL.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.x, pos.y, pos.z, 10., level.dimension())),
                new ClientboundUpdatePlayersPacket(joinedPlayers.stream().map(ServerPlayer::getUUID).toArray(UUID[]::new)));
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void startMatch() {
        if (isMatching()) return;
        if (joinedPlayers.size() % 2 != 0 && joinedPlayers.size() != 1) return;
        dividePlayers();
        nextTurn();
        setMatching(true);
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void endMatch() {
        joinedPlayers.clear();
        turn = null;
        white = null;
        black = null;
        board = null;
        setMatching(false);
        matchJustEnded = true;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    private void dividePlayers() {
        if (joinedPlayers.size() == 1) {
            var p = joinedPlayers.iterator().next();
            addToWhite(p);
            addToBlack(p);
        } else {
            Random r = new Random();
            var joinedPlayers = new ObjectArrayList<>(this.joinedPlayers);
            for (int i = 0, size = joinedPlayers.size(), c = size / 2; i < c; i++) {
                Player w = joinedPlayers.get(r.nextInt(size--));
                joinedPlayers.remove(w);
                addToWhite(w);
                Player b = joinedPlayers.get(r.nextInt(size--));
                joinedPlayers.remove(b);
                addToBlack(b);
            }
        }
    }

    private void addToWhite(Player p) {
        if (white == null) this.white = new ObjectOpenHashSet<>();
        white.add(p);
    }

    private void addToBlack(Player p) {
        if (black == null) this.black = new ObjectOpenHashSet<>();
        black.add(p);
    }

    public void nextTurn() {
        if (turn == null) turn = PieceType.BLACK;
        else turn = turn == PieceType.BLACK ? PieceType.WHITE : PieceType.BLACK;
        setChanged();
    }

    public boolean isTurnFor(Player player) {
        return isMatching() && (turn == PieceType.BLACK ? black.contains(player) : white.contains(player));
    }

    @UsedOn(UsedOn.Side.SERVER)
    public void placePiece(Player p, byte x, byte z) {
        if (isMatching() && isTurnFor(p) && getBoard().putPiece(x, z, turn)) {
            if (getBoard().checkWin(x, z)) {
                if (turn == PieceType.WHITE) {
                    joinedPlayers.forEach(player -> player.connection.send(new ClientboundSetTitleTextPacket(WHITE_WIN)));
                } else {
                    joinedPlayers.forEach(player -> player.connection.send(new ClientboundSetTitleTextPacket(BLACK_WIN)));
                }
                endMatch();
                return;
            }
            nextTurn();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.getBoolean(CLEAR_KEY)) {
            this.board = null;
            this.matching = false;
            this.turn = null;
            this.white = null;
            this.black = null;
            return;
        }
        if (tag.contains(BOARD_KEY, Tag.TAG_COMPOUND)) {
            this.board = Board.load(this::setChanged, tag.getCompound(BOARD_KEY));
        }
        if (tag.contains(MATCHING_KEY, Tag.TAG_BYTE)) {
            this.matching = tag.getBoolean(MATCHING_KEY);
        }
        if (tag.contains(TURN_KEY, Tag.TAG_BYTE)) {
            this.turn = PieceType.getFromCode(tag.getByte(TURN_KEY));
        }
        if (tag.contains(WHITE_KEY, Tag.TAG_COMPOUND)) {
            this.white = getPlayerList(tag, WHITE_KEY);
        }
        if (tag.contains(BLACK_KEY, Tag.TAG_COMPOUND)) {
            this.black = getPlayerList(tag, BLACK_KEY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (board != null) {
            tag.put(BOARD_KEY, board.save());
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = saveWithoutMetadata();
        if (matchJustEnded) {
            tag.putBoolean(CLEAR_KEY, true);
            this.matchJustEnded = false;
        }
        if (matching) {
            tag.putBoolean(MATCHING_KEY, true);
        }
        if (turn != null) {
            tag.putByte(TURN_KEY, turn.code);
        }
        putPlayerList(tag, white, WHITE_KEY);
        putPlayerList(tag, black, BLACK_KEY);
        return tag;
    }

    private static void putPlayerList(CompoundTag tag, @Nullable Set<Player> set, String key) {
        if (set != null && !set.isEmpty()) {
            CompoundTag compound = new CompoundTag();
            var list = List.copyOf(set);
            for (int i = 0; i < set.size(); i++) {
                compound.putUUID(String.valueOf(i), list.get(i).getUUID());
            }
            tag.put(key, compound);
        }
    }

    private Set<Player> getPlayerList(CompoundTag tag, String key) {
        CompoundTag compound = tag.getCompound(key);
        Set<Player> list = new ObjectOpenHashSet<>(compound.size());
        for (String k : compound.getAllKeys()) {
            Player p = level.getPlayerByUUID(compound.getUUID(k));
            if (p != null) list.add(p);
        }
        return list;
    }
}