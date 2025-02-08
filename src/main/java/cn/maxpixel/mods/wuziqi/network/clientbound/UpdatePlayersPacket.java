package cn.maxpixel.mods.wuziqi.network.clientbound;

import cn.maxpixel.mods.wuziqi.client.screen.PrepareMatchScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.maxpixel.mods.wuziqi.WuziqiMod.rl;

public record UpdatePlayersPacket(List<UUID> players) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdatePlayersPacket> TYPE = new CustomPacketPayload.Type<>(rl("update_player"));
    public static final StreamCodec<ByteBuf, UpdatePlayersPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, UUIDUtil.STREAM_CODEC),
            UpdatePlayersPacket::players,
            UpdatePlayersPacket::new
    );

    public static void handle(UpdatePlayersPacket message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> clientHandle(message));
        }
    }

    public static void clientHandle(UpdatePlayersPacket message) {
        var mc = Minecraft.getInstance();
        if (mc.screen instanceof PrepareMatchScreen screen) {
            screen.setQueuedPlayers(message.players.toArray(new UUID[0]));
        }
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
