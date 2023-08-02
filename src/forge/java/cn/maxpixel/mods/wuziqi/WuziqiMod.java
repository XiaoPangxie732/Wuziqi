package cn.maxpixel.mods.wuziqi;

import cn.maxpixel.mods.wuziqi.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WuziqiMod.MODID)
public class WuziqiMod {
    public static final String MODID = "wuziqi";

    public WuziqiMod() {
        Registries.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(WuziqiMod.MODID, path);
    }
}