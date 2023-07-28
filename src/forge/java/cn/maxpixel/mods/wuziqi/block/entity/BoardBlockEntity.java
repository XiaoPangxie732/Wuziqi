package cn.maxpixel.mods.wuziqi.block.entity;

import cn.maxpixel.mods.wuziqi.BoardStore;
import cn.maxpixel.mods.wuziqi.PieceType;
import cn.maxpixel.mods.wuziqi.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

import static cn.maxpixel.mods.wuziqi.BoardSizeSetting.SETTING;

public class BoardBlockEntity extends BlockEntity {

    public BoardStore store = SETTING.makeBoard();

    public BoardBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BOARD.get(), pos, state);
    }

    public void onHit(Vec2 hitPos, Player player) {
        var item = player.getMainHandItem().getItem();
        if (item == Items.BLACK_WOOL) {
            store.setPiece(hitPos, PieceType.BLACK);
        } else if (item == Items.WHITE_WOOL) {
            store.setPiece(hitPos, PieceType.WHITE);
        } else {
            store.setPiece(hitPos, PieceType.EMPTY);
        }
    }
}