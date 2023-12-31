package cn.maxpixel.mods.wuziqi.block;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import cn.maxpixel.mods.wuziqi.client.screen.Screens;
import cn.maxpixel.mods.wuziqi.network.Network;
import cn.maxpixel.mods.wuziqi.network.serverbound.ServerboundPiecePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BoardBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = box(1, 0, 1, 15, 1, 15);

    public BoardBlock(Properties prop) {
        super(prop);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoardBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);// TODO: big boards
    }

    @Override
    public void randomTick(BlockState p_222954_, ServerLevel p_222955_, BlockPos p_222956_, RandomSource p_222957_) {
        // generally this wouldn't be called
        // just in case, leave this noop
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {// see BoardBlockEntity.startMatch
        if (level.getExistingBlockEntity(pos) instanceof BoardBlockEntity be) {
            be.endMatch();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;
        if (level.getExistingBlockEntity(pos) instanceof BoardBlockEntity blockEntity) {
            if (level.isClientSide) {
                if (!blockEntity.isMatching()) {
                    Screens.openPrepareMatchScreen(blockEntity);
                    return InteractionResult.SUCCESS;
                }
                var loc = hitResult.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
                if (!checkHitLocation(loc)) return InteractionResult.PASS;
                int x = getPos(loc.x);
                int z = getPos(loc.z);
                if (blockEntity.getBoard().hasPiece(x, z)) return InteractionResult.PASS;
                Network.CHANNEL.sendToServer(new ServerboundPiecePacket(pos, ServerboundPiecePacket.Action.PLACE, (byte) x, (byte) z));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public static boolean checkHitLocation(Vec3 loc) {
        return Mth.equal(loc.y, 1.f / 16.f) && checkHitLocation(loc.x) && checkHitLocation(loc.z);
    }

    private static boolean checkHitLocation(double d) {
        return d >= 1. / 16. && d <= 15. / 16.;
    }

    public static int getPos(double d) {
        // double range = ((max * 1.0) / BLOCK_LENGTH) - ((min * 1.0) / BLOCK_LENGTH);
        // var normalizedX = (x - (min * 1.0) / BLOCK_LENGTH) / range;
        // var normalizedZ = (z - (min * 1.0) / BLOCK_LENGTH) / range;
        // var finalX = (int) Math.round(normalizedX * (length - 1) + 1);
        // var finalZ = (int) Math.round(normalizedZ * (length - 1) + 1);
        return (int) Math.round((d - 1. / 16.) / (14. / 16.) * 14.);
    }
}