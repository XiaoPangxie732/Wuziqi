package cn.maxpixel.mods.wuziqi.client.renderer.entity.block;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BoardBlockEntityRenderer implements BlockEntityRenderer<BoardBlockEntity> {
    public BoardBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BoardBlockEntity block, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    }
}