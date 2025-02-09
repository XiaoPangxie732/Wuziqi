package cn.maxpixel.mods.wuziqi.registry;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WuziqiMod.MODID);

    public static final Supplier<BlockEntityType<BoardBlockEntity>> BOARD = BLOCK_ENTITY_TYPES.register("board", () -> BlockEntityType.Builder.of(BoardBlockEntity::new,
            BlockRegistry.OAK_BOARD.get()).build(null));
}
