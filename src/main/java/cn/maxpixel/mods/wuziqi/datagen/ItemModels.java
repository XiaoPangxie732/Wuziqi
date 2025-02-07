package cn.maxpixel.mods.wuziqi.datagen;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WuziqiMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {// Currently no use
    }
}
