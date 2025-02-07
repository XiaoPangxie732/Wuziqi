package cn.maxpixel.mods.wuziqi.datagen;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.datagen.lang.AmericanEnglishProvider;
import cn.maxpixel.mods.wuziqi.datagen.lang.SimplifiedChineseProvider;
import cn.maxpixel.mods.wuziqi.datagen.tag.BlockTags;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WuziqiMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntryPoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFile = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), (DataProvider.Factory<AmericanEnglishProvider>) AmericanEnglishProvider::new);
        generator.addProvider(event.includeClient(), (DataProvider.Factory<SimplifiedChineseProvider>) SimplifiedChineseProvider::new);
        generator.addProvider(event.includeClient(), new BlockStates(output, existingFile));
        generator.addProvider(event.includeClient(), new ItemModels(output, existingFile));

        generator.addProvider(event.includeServer(), (DataProvider.Factory<LootTables>) LootTables::new);
        generator.addProvider(event.includeServer(), new BlockTags(output, event.getLookupProvider(), existingFile));
    }
}