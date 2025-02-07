package cn.maxpixel.mods.wuziqi;

import cn.maxpixel.mods.wuziqi.registry.Registries;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(WuziqiMod.MODID)
public class WuziqiMod {
    public static final String MODID = "wuziqi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WuziqiMod(IEventBus modEventBus, ModContainer modContainer) {
        Registries.register(modEventBus);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(WuziqiMod.MODID, path);
    }
}