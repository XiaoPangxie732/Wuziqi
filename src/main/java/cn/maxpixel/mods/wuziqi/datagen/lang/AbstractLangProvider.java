package cn.maxpixel.mods.wuziqi.datagen.lang;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class AbstractLangProvider extends LanguageProvider {
    public AbstractLangProvider(PackOutput output, String locale) {
        super(output, WuziqiMod.MODID, locale);
    }

    public void addItemGroup(String key, String name) {
        add(I18nUtil.makeItemGroup(key), name);
    }

    public void addScreen(String screen, String key, String name) {
        add(I18nUtil.makeScreenText(screen, key), name);
    }
}