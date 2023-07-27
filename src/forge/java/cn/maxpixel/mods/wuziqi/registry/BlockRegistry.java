package cn.maxpixel.mods.wuziqi.registry;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.block.BoardBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WuziqiMod.MODID);

    public static final RegistryObject<Block> OAK_BOARD = registerWithItem("oak_board", () -> new BoardBlock(BlockBehaviour.Properties.of()
            .strength(2.0F, 3.0F)
            .noOcclusion()
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.DESTROY)// TODO: maybe change this later
            .isValidSpawn(BlockRegistry::never)
            .isRedstoneConductor(BlockRegistry::never)
            .isSuffocating(BlockRegistry::never)
            .ignitedByLava()));

    private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> supplier) {
        return registerWithItem(name, supplier, Item.Properties::new);
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> supplier, Supplier<Item.Properties> itemProp) {
        RegistryObject<T> obj = BLOCKS.register(name, supplier);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(obj.get(), itemProp.get()));
        return obj;
    }

    private static boolean never(BlockState p_61036_, BlockGetter p_61037_, BlockPos p_61038_) {
        return false;
    }

    private static boolean never(BlockState p_61031_, BlockGetter p_61032_, BlockPos p_61033_, EntityType<?> p_61034_) {
        return false;
    }
}