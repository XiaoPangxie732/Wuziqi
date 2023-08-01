package cn.maxpixel.mods.wuziqi.network.clientbound;

import cn.maxpixel.mods.wuziqi.client.screen.PrepareMatchScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundUpdatePlayersPacket {
    private final UUID[] players;

    public ClientboundUpdatePlayersPacket(UUID[] players) {
        this.players = players;
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        ctxSupplier.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> this::clientHandle));
        ctxSupplier.get().setPacketHandled(true);
    }

    public void clientHandle() {
        var mc = Minecraft.getInstance();
        if (mc.screen instanceof PrepareMatchScreen screen) {
            screen.setQueuedPlayers(players);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(players.length);
        for (UUID player : players) {
            buf.writeUUID(player);
        }
    }

    public static ClientboundUpdatePlayersPacket decode(FriendlyByteBuf buf) {
        UUID[] players = new UUID[buf.readVarInt()];
        for (int i = 0; i < players.length; i++) {
            players[i] = buf.readUUID();
        }
        return new ClientboundUpdatePlayersPacket(players);
    }
}