package cn.maxpixel.mods.wuziqi.client.renderer.entity.block;

import cn.maxpixel.mods.wuziqi.BoardSizeSetting;
import cn.maxpixel.mods.wuziqi.BoardStore;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class BoardBlockEntityRenderer implements BlockEntityRenderer<BoardBlockEntity> {
    public BoardBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(BoardBlockEntity blockEntity, float partialTick, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        renderLines(pose, blockEntity.store);
        var render = Minecraft.getInstance().getBlockRenderer();
        var setting = blockEntity.store.setting;
        pose.pushPose();
        pose.translate(-(1.0 / BoardSizeSetting.BLOCK_LENGTH / 2), setting.height * 1.0 / BoardSizeSetting.BLOCK_LENGTH, -(1.0 / BoardSizeSetting.BLOCK_LENGTH / 2));
        for (int x = 1; x <= setting.length; x++) {
            for (int z = 1; z <= setting.length; z++) {
                var localPos = setting.calculatePercentPos(x, z);
                pose.pushPose();
                pose.translate(localPos.x, 0, localPos.y);
                var scale = 1.0f / BoardSizeSetting.BLOCK_LENGTH;
                pose.scale(scale, scale, scale);
                switch (blockEntity.store.getSlotPiece(x, z)) {
                    case EMPTY -> {
                    }
                    case BLACK -> {
                        render.renderSingleBlock(Blocks.BLACK_WOOL.defaultBlockState(), pose,
                                buffer, packedLight, packedOverlay, ModelData.EMPTY,
                                RenderType.solid());
                    }
                    case WHITE -> {
                        render.renderSingleBlock(Blocks.WHITE_WOOL.defaultBlockState(), pose,
                                buffer, packedLight, packedOverlay, ModelData.EMPTY,
                                RenderType.solid());
                    }
                }
                pose.popPose();
            }
        }
        pose.popPose();
    }

    private void renderLines(PoseStack poseStack, BoardStore store) {
        Matrix4f pose = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        var setting = store.setting;
        for (int i = 1; i <= setting.length; i++) {
            {
                var fromPos = setting.calculatePercentPos(i, 1);
                var toPos = setting.calculatePercentPos(i, setting.length);
                renderXSameLine(pose, builder, 0x55555555, 0.01f, fromPos, toPos, setting.height * 1.0f / BoardSizeSetting.BLOCK_LENGTH + 0.01f);
            }
            {
                var fromPos = setting.calculatePercentPos(1, i);
                var toPos = setting.calculatePercentPos(setting.length, i);
                renderZSameLine(pose, builder, 0x55555555, 0.01f, fromPos, toPos, setting.height * 1.0f / BoardSizeSetting.BLOCK_LENGTH + 0.01f);
            }
        }
        tesselator.end();
    }

    private void renderXSameLine(Matrix4f pose, BufferBuilder consumer, int color, float halfWidth, Vec2 from, Vec2 to, float relateHeight) {
        //from.x === to.x from.y < to.y
        consumer.vertex(pose, from.x - halfWidth, relateHeight, from.y).color(color).endVertex();
        consumer.vertex(pose, to.x - halfWidth, relateHeight, to.y).color(color).endVertex();
        consumer.vertex(pose, to.x + halfWidth, relateHeight, to.y).color(color).endVertex();
        consumer.vertex(pose, from.x + halfWidth, relateHeight, from.y).color(color).endVertex();
    }

    private void renderZSameLine(Matrix4f pose, BufferBuilder consumer, int color, float halfWidth, Vec2 from, Vec2 to, float relateHeight) {
        //from.y === to.y from.x < to.x
        consumer.vertex(pose, from.x, relateHeight, from.y - halfWidth).color(color).endVertex();
        consumer.vertex(pose, from.x, relateHeight, from.y + halfWidth).color(color).endVertex();
        consumer.vertex(pose, to.x, relateHeight, to.y + halfWidth).color(color).endVertex();
        consumer.vertex(pose, to.x, relateHeight, to.y - halfWidth).color(color).endVertex();
    }
}