package cn.maxpixel.mods.wuziqi.registry;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WuziqiMod.MODID);

    public static final RegistryObject<BlockEntityType<BoardBlockEntity>> BOARD = BLOCK_ENTITY_TYPES.register("board", () -> BlockEntityType.Builder.of(BoardBlockEntity::new,
            BlockRegistry.OAK_BOARD.get()).build(null));
}
