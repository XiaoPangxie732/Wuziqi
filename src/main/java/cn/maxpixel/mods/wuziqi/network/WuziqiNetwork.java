package cn.maxpixel.mods.wuziqi.network;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.network.clientbound.UpdatePlayersPacket;
import cn.maxpixel.mods.wuziqi.network.serverbound.PiecePacket;
import cn.maxpixel.mods.wuziqi.network.serverbound.PrepareMatchPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WuziqiMod.MODID,bus = EventBusSubscriber.Bus.MOD)
public class WuziqiNetwork {
    @SubscribeEvent
    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar(WuziqiMod.MODID).versioned("1").optional();
        registrar.playToClient(UpdatePlayersPacket.TYPE,UpdatePlayersPacket.STREAM_CODEC, UpdatePlayersPacket::handle);
        registrar.playToServer(PiecePacket.TYPE,PiecePacket.STREAM_CODEC, PiecePacket::handle);
        registrar.playToServer(PrepareMatchPacket.TYPE,PrepareMatchPacket.STREAM_CODEC, PrepareMatchPacket::handle);
    }
}
