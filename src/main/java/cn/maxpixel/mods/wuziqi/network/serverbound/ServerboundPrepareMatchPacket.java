package cn.maxpixel.mods.wuziqi.network.serverbound;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundPrepareMatchPacket {
    private final Action action;
    private final BlockPos boardPos;

    public ServerboundPrepareMatchPacket(Action action, BlockPos boardPos) {
        this.action = action;
        this.boardPos = boardPos;
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        var ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            var sender = ctx.getSender();
            if (sender != null && sender.level().getExistingBlockEntity(boardPos) instanceof BoardBlockEntity blockEntity) {
                var level = sender.level();
                switch (action) {
                    case JOIN -> blockEntity.addPlayer(sender);
                    case QUIT -> blockEntity.removePlayer(sender);
                    case START -> blockEntity.startMatch();
                    case SYNC -> blockEntity.syncJoinedPlayers();
                }
                var state = level.getBlockState(boardPos);
                level.sendBlockUpdated(boardPos, state, state, Block.UPDATE_CLIENTS);
            }
        });
        ctx.setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(action).writeBlockPos(boardPos);
    }

    public static ServerboundPrepareMatchPacket decode(FriendlyByteBuf buf) {
        return new ServerboundPrepareMatchPacket(buf.readEnum(Action.class), buf.readBlockPos());
    }

    public enum Action {
        JOIN, QUIT, SYNC, START
    }
}