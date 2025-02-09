package cn.maxpixel.mods.wuziqi.datagen.loot;

import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import java.util.Set;

public class BlockLoots extends BlockLootSubProvider {
    public BlockLoots(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(),provider);
    }

    @Override
    protected void generate() {
        dropSelf(BlockRegistry.OAK_BOARD.get());
    }
}