package cn.maxpixel.mods.wuziqi.network.serverbound;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundPiecePacket {
    private final BlockPos pos;
    private final Action action;
    private final byte x;
    private final byte z;

    public ServerboundPiecePacket(BlockPos pos, Action action, byte x, byte z) {
        this.pos = pos;
        this.action = action;
        this.x = x;
        this.z = z;
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        var ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            var level = ctx.getSender().level();
            if (level.getExistingBlockEntity(pos) instanceof BoardBlockEntity blockEntity) {
                blockEntity.placePiece(ctx.getSender(), x, z);
                var state = level.getBlockState(pos);
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        });
        ctx.setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos).writeEnum(action).writeByte(x).writeByte(z);
    }

    public static ServerboundPiecePacket decode(FriendlyByteBuf buf) {
        return new ServerboundPiecePacket(buf.readBlockPos(), buf.readEnum(Action.class), buf.readByte(), buf.readByte());
    }

    public enum Action {
        PLACE, REMOVE
    }
}