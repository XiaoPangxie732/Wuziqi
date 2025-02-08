package cn.maxpixel.mods.wuziqi.network.serverbound;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static cn.maxpixel.mods.wuziqi.WuziqiMod.rl;

public record PiecePacket(BlockPos pos, Action action, byte x, byte z) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PiecePacket> TYPE = new CustomPacketPayload.Type<>(rl("piece"));
    public static final StreamCodec<FriendlyByteBuf, PiecePacket> STREAM_CODEC = StreamCodec.composite(
            net.minecraft.core.BlockPos.STREAM_CODEC,
            PiecePacket::pos,
            NeoForgeStreamCodecs.enumCodec(Action.class),
            PiecePacket::action,
            ByteBufCodecs.BYTE,
            PiecePacket::x,
            ByteBufCodecs.BYTE,
            PiecePacket::z,
            PiecePacket::new
    );

    public static void handle(PiecePacket message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                var level = context.player().level();
                BlockPos pos = message.pos;
                if (level.getBlockEntity(pos) instanceof BoardBlockEntity blockEntity) {
                    blockEntity.placePiece(context.player(), message.x(), message.z());
                    var state = level.getBlockState(pos);
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {
        PLACE, REMOVE
    }
}
