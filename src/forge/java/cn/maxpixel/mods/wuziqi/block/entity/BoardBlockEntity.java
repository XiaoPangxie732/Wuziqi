package cn.maxpixel.mods.wuziqi.block.entity;

import cn.maxpixel.mods.wuziqi.board.wuziqi.Board;
import cn.maxpixel.mods.wuziqi.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BoardBlockEntity extends BlockEntity {
    public static final String BOARD_KEY = "Board";
    public static final String MATCHING_KEY = "Matching";
    private @Nullable Board board;
    private boolean matching;

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

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(BOARD_KEY, Tag.TAG_COMPOUND)) {
            this.board = Board.load(this::setChanged, tag.getCompound(BOARD_KEY));
        }
        if (tag.contains(MATCHING_KEY, Tag.TAG_BYTE)) {
            this.matching = tag.getBoolean(MATCHING_KEY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (board != null) {
            tag.put(BOARD_KEY, board.save());
        }
        if (matching) {
            tag.putBoolean(MATCHING_KEY, true);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}