package cn.maxpixel.mods.wuziqi.datagen;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockStates extends BlockStateProvider {
    public BlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WuziqiMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(BlockRegistry.OAK_BOARD.get(), models().singleTexture(BlockRegistry.OAK_BOARD.getId().getPath(), mcLoc("block/pressure_plate_up"),
                ForgeRegistries.BLOCKS.getKey(Blocks.STRIPPED_OAK_LOG).withPrefix("block/")));
    }
}