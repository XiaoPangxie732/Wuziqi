package cn.maxpixel.mods.wuziqi.datagen.lang;

import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import cn.maxpixel.mods.wuziqi.registry.CreativeModeTabRegistry;
import net.minecraft.data.PackOutput;

public class AmericanEnglishProvider extends AbstractLangProvider {
    public AmericanEnglishProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItemGroup(CreativeModeTabRegistry.GENERAL_NAME, "Wuziqi");

        addBlock(BlockRegistry.OAK_BOARD, "Oak board");
    }
}
