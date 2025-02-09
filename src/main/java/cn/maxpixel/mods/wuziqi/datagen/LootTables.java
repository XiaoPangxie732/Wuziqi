package cn.maxpixel.mods.wuziqi.datagen;

import cn.maxpixel.mods.wuziqi.datagen.loot.BlockLoots;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LootTables extends LootTableProvider {
    public LootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockLoots::new, LootContextParamSets.BLOCK)),provider);
    }
}