package cn.maxpixel.mods.wuziqi.network;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.network.clientbound.ClientboundUpdatePlayersPacket;
import cn.maxpixel.mods.wuziqi.network.serverbound.ServerboundPiecePacket;
import cn.maxpixel.mods.wuziqi.network.serverbound.ServerboundPrepareMatchPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Network {
    private static final AtomicInteger INDEX = new AtomicInteger();
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            WuziqiMod.rl("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    static {
        // clientbound
        register(ClientboundUpdatePlayersPacket.class, ClientboundUpdatePlayersPacket::encode, ClientboundUpdatePlayersPacket::decode, ClientboundUpdatePlayersPacket::handle);

        // serverbound
        register(ServerboundPrepareMatchPacket.class, ServerboundPrepareMatchPacket::encode, ServerboundPrepareMatchPacket::decode, ServerboundPrepareMatchPacket::handle);
        register(ServerboundPiecePacket.class, ServerboundPiecePacket::encode, ServerboundPiecePacket::decode, ServerboundPiecePacket::handle);
    }

    private static <MSG> void register(Class<MSG> type, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder,
                                       BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(INDEX.getAndIncrement(), type, encoder, decoder, messageConsumer);
    }

    public static void init() {}// call <clinit>
}