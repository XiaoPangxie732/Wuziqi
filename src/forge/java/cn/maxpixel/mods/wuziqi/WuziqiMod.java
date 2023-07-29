package cn.maxpixel.mods.wuziqi;

import cn.maxpixel.mods.wuziqi.registry.Registries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WuziqiMod.MODID)
public class WuziqiMod {
    public static final String MODID = "wuziqi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WuziqiMod() {
        Registries.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}