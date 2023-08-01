package cn.maxpixel.mods.wuziqi.util;

import cn.maxpixel.mods.wuziqi.WuziqiMod;

public class I18nUtil {
    public static String make(String category, String path) {
        return category + '.' + WuziqiMod.MODID + '.' + path;
    }

    public static String makeItemGroup(String name) {
        return make("itemGroup", name);
    }

    public static String makeScreenText(String screen, String name) {
        return make("screen", screen + '.' + name);
    }
}