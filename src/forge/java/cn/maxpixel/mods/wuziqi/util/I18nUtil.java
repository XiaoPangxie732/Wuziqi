package cn.maxpixel.mods.wuziqi.util;

import cn.maxpixel.mods.wuziqi.WuziqiMod;

public class I18nUtil {
    public static String makeItemGroup(String name) {
        return "itemGroup." + WuziqiMod.MODID + '.' + name;
    }
}