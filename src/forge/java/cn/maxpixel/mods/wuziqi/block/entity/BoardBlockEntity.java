package cn.maxpixel.mods.wuziqi.block.entity;

import cn.maxpixel.mods.wuziqi.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BoardBlockEntity extends BlockEntity {
    public BoardBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BOARD.get(), pos, state);
    }
}