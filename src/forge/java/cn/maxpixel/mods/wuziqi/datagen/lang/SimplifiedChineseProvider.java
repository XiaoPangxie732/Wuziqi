package cn.maxpixel.mods.wuziqi.datagen.lang;

import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import cn.maxpixel.mods.wuziqi.registry.CreativeModeTabRegistry;
import net.minecraft.data.PackOutput;

public class SimplifiedChineseProvider extends AbstractLangProvider {
    public SimplifiedChineseProvider(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addItemGroup(CreativeModeTabRegistry.GENERAL_NAME, "五子棋");

        addBlock(BlockRegistry.OAK_BOARD, "橡木棋盘");
    }
}