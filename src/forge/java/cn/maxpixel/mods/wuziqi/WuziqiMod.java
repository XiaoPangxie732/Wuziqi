package cn.maxpixel.mods.wuziqi;

import cn.maxpixel.mods.wuziqi.registry.Registries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WuziqiMod.MODID)
public class WuziqiMod {
    public static final String MODID = "wuziqi";

    public WuziqiMod() {
        Registries.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}