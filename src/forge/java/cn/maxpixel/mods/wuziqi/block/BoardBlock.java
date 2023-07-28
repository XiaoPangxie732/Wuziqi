package cn.maxpixel.mods.wuziqi.block;

import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.maxpixel.mods.wuziqi.BoardSizeSetting.SETTING;

public class BoardBlock extends BaseEntityBlock {
    public BoardBlock(Properties prop) {
        super(prop);
    }

    VoxelShape shape = box(SETTING.min, 0, SETTING.min,
            SETTING.max, SETTING.height, SETTING.max);

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BoardBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return shape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;
        var hitAbsoluteLocation = hitResult.getLocation();
        var hitLocalLocation = hitAbsoluteLocation.subtract(pos.getX(), pos.getY(), pos.getZ());
        if (!SETTING.checkHitLocation(hitLocalLocation)) return InteractionResult.PASS;

        WuziqiMod.LOGGER.info("hitLocation : x:{}, y:{}, z:{}", hitLocalLocation.x, hitLocalLocation.y, hitLocalLocation.z);
        if (level.getBlockEntity(pos) instanceof BoardBlockEntity boardBlockEntity) {
            var hitPos = SETTING.calculatePos(hitLocalLocation);
            WuziqiMod.LOGGER.info("hit pos x:{},y:{}", (int) hitPos.x, (int) hitPos.y);
            WuziqiMod.LOGGER.info("restore pos x:{},y:{}", SETTING.calculatePercentPos(hitPos).x, SETTING.calculatePercentPos(hitPos).y);
            boardBlockEntity.onHit(hitPos, player);
            WuziqiMod.LOGGER.info("currentPiece:{}", boardBlockEntity.store.getSlotPiece(hitPos));
        }
        return InteractionResult.SUCCESS;
    }
}