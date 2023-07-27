package cn.maxpixel.mods.wuziqi.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registries {
    public static void register(IEventBus modBus) {
        BlockRegistry.BLOCKS.register(modBus);
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        ItemRegistry.ITEMS.register(modBus);
        CreativeModeTabRegistry.TABS.register(modBus);
    }
}