package cn.maxpixel.mods.wuziqi.datagen.loot;

import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlockLoots extends BlockLootSubProvider {
    public BlockLoots() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BlockRegistry.OAK_BOARD.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries()
                .stream()
                .<Block>mapMulti(RegistryObject::ifPresent)
                .toList();
    }
}