package cn.maxpixel.mods.wuziqi.registry;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WuziqiMod.MODID);

    public static final String GENERAL_NAME = "general";
    public static final Component GENERAL_TITLE = Component.translatable(I18nUtil.makeItemGroup(GENERAL_NAME));
    public static final RegistryObject<CreativeModeTab> GENERAL = TABS.register(GENERAL_NAME, () -> CreativeModeTab.builder()
            .title(GENERAL_TITLE)
            .displayItems((params, output) -> {
                output.accept(BlockRegistry.OAK_BOARD.get());
            }).build());
}