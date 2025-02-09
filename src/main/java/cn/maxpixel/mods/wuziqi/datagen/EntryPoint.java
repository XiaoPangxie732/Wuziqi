package cn.maxpixel.mods.wuziqi.datagen;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.datagen.lang.AmericanEnglishProvider;
import cn.maxpixel.mods.wuziqi.datagen.lang.SimplifiedChineseProvider;
import cn.maxpixel.mods.wuziqi.datagen.tag.BlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = WuziqiMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EntryPoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFile = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), (DataProvider.Factory<AmericanEnglishProvider>) AmericanEnglishProvider::new);
        generator.addProvider(event.includeClient(), (DataProvider.Factory<SimplifiedChineseProvider>) SimplifiedChineseProvider::new);
        generator.addProvider(event.includeClient(), new BlockStates(output, existingFile));
        generator.addProvider(event.includeClient(), new ItemModels(output, existingFile));

        generator.addProvider(event.includeServer(), new LootTables(output,lookupProvider));
        generator.addProvider(event.includeServer(), new BlockTags(output, event.getLookupProvider(), existingFile));
    }
}