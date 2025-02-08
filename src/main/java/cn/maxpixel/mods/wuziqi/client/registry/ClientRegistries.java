package cn.maxpixel.mods.wuziqi.client.registry;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.client.renderer.entity.block.BoardBlockEntityRenderer;
import cn.maxpixel.mods.wuziqi.registry.BlockEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = WuziqiMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistries {
    @SubscribeEvent
    public static void onEntityRenderersRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.BOARD.get(), BoardBlockEntityRenderer::new);
    }
}
