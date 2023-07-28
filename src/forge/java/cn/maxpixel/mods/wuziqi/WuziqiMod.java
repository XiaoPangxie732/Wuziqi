package cn.maxpixel.mods.wuziqi;

import cn.maxpixel.mods.wuziqi.registry.Registries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(WuziqiMod.MODID)
public class WuziqiMod {
    public static final String MODID = "wuziqi";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public WuziqiMod() {
        Registries.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}