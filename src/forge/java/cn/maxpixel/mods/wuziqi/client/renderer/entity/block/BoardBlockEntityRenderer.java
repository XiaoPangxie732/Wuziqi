package cn.maxpixel.mods.wuziqi.client.renderer.entity.block;

import cn.maxpixel.mods.wuziqi.block.BoardBlock;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import cn.maxpixel.mods.wuziqi.board.Board;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;

public class BoardBlockEntityRenderer implements BlockEntityRenderer<BoardBlockEntity> {
    public static final float BLOCK_LENGTH = 16.f;
    public static final float STEP = (1 - 2.f / BLOCK_LENGTH) / (Board.BOARD_SIZE - 1);
    private static final int FRAGMENTS = 32;
    private static final float RADIUS = 1.0f / 16.f / 2.f;
    private static final int INDICATOR_COLOR = 0xFFFFFFFF;
    private static final int INDICATOR_DISABLED_COLOR = 0xFFD02525;
    private static final int GRID_COLOR = 0xFF000000;
    private static final float GRID_WIDTH_HALF = .005f;
    private static final float GRID_HEIGHT = 1.f / BLOCK_LENGTH + .001f;
    private static final float GRID_POS_MIN = getPos(0);
    private static final float GRID_POS_MAX = getPos(Board.BOARD_SIZE - 1);
    private static final Component YOUR_TURN = Component.translatable(I18nUtil.make("block_entity", "board.your_turn"));
    private static final Component OPPONENTS_TURN = Component.translatable(I18nUtil.make("block_entity", "board.opponents_turn"));

    private final BlockEntityRendererProvider.Context context;
    public BoardBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BoardBlockEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        pose.pushPose();
        pose.scale(1.f, 1.f, 1.f);
        renderLines(pose);
        pose.pushPose();
        pose.translate(-(1.f / BLOCK_LENGTH / 2), 1.f / BLOCK_LENGTH, -(1.f / BLOCK_LENGTH / 2));
        var render = context.getBlockRenderDispatcher();
        for (int x = 0; x < Board.BOARD_SIZE; x++) {
            for (int z = 0; z < Board.BOARD_SIZE; z++) {
                pose.pushPose();
                pose.translate(getPos(x), -.5f / BLOCK_LENGTH, getPos(z));
                var scale = 1.f / BLOCK_LENGTH;
                pose.scale(scale, scale, scale);
                var piece = blockEntity.getBoard().getPiece(x, z);
                if (piece != null) {
                    switch (piece) {
                        case BLACK -> render.renderSingleBlock(Blocks.BLACK_CONCRETE.defaultBlockState(), pose,
                                buffer, packedLight, packedOverlay, ModelData.EMPTY, RenderType.solid());
                        case WHITE -> render.renderSingleBlock(Blocks.WHITE_CONCRETE.defaultBlockState(), pose,
                                buffer, packedLight, packedOverlay, ModelData.EMPTY, RenderType.solid());
                    }
                }
                pose.popPose();
            }
        }
        pose.popPose();
        if (blockEntity.isMatching()) {
            renderIndicator(pose, blockEntity);
            renderStatus(pose, Component.translatable(I18nUtil.make("block_entity", "board.status"),
                    blockEntity.isTurnFor(Minecraft.getInstance().player) ? YOUR_TURN : OPPONENTS_TURN), buffer, packedLight);
        }
        pose.popPose();
    }

    private static void renderLines(PoseStack poseStack) {
        Matrix4f pose = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        var depthState = GL46.glGetBoolean(GL46.GL_DEPTH_TEST);
        if (!depthState) RenderSystem.enableDepthTest();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            float pos = getPos(i);
            renderZAxisLine(pose, builder, pos);
            renderXAxisLine(pose, builder, pos);
        }
        tesselator.end();
        if (!depthState) RenderSystem.disableDepthTest();
    }

    private static float getPos(int i) {
        return 1.f / BLOCK_LENGTH + i * STEP;
    }

    private static void renderZAxisLine(Matrix4f pose, BufferBuilder consumer, float x) {
        consumer.vertex(pose, x - GRID_WIDTH_HALF, GRID_HEIGHT, GRID_POS_MIN).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, x - GRID_WIDTH_HALF, GRID_HEIGHT, GRID_POS_MAX).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, x + GRID_WIDTH_HALF, GRID_HEIGHT, GRID_POS_MAX).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, x + GRID_WIDTH_HALF, GRID_HEIGHT, GRID_POS_MIN).color(GRID_COLOR).endVertex();
    }

    private static void renderXAxisLine(Matrix4f pose, BufferBuilder consumer, float z) {
        consumer.vertex(pose, GRID_POS_MIN, GRID_HEIGHT, z - GRID_WIDTH_HALF).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, GRID_POS_MIN, GRID_HEIGHT, z + GRID_WIDTH_HALF).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, GRID_POS_MAX, GRID_HEIGHT, z + GRID_WIDTH_HALF).color(GRID_COLOR).endVertex();
        consumer.vertex(pose, GRID_POS_MAX, GRID_HEIGHT, z - GRID_WIDTH_HALF).color(GRID_COLOR).endVertex();
    }

    private static void renderIndicator(PoseStack poseStack, BoardBlockEntity blockEntity) {
        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null || mc.player == null || mc.player.isSpectator()) return;
        var hr = mc.hitResult;
        if (hr == null || hr.getType() != HitResult.Type.BLOCK) return;
        var blockHitResult = ((BlockHitResult) hr);
        var blockPos = blockHitResult.getBlockPos();
        if (level.getExistingBlockEntity(blockPos) == blockEntity) {
            var loc = blockHitResult.getLocation().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (!BoardBlock.checkHitLocation(loc)) return;
            int boardX = BoardBlock.getPos(loc.x);
            int boardZ = BoardBlock.getPos(loc.z);
            if (blockEntity.getBoard().hasPiece(boardX, boardZ)) return;
            int indicatorColor = blockEntity.isTurnFor(mc.player) ? INDICATOR_COLOR : INDICATOR_DISABLED_COLOR;

            poseStack.pushPose();
            poseStack.translate(getPos(boardX), (1.f / BLOCK_LENGTH) + 0.002f, getPos(boardZ));

            // draw begin
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

            // fill vertex
            float eachRadius = (float) (Math.PI * 2 / FRAGMENTS);
            var pose = poseStack.last().pose();
            for (int i = 0; i < FRAGMENTS; i++) {
                var currentRadius = eachRadius * i;
                var nextRadius = eachRadius * (i + 1);
                builder.vertex(pose, 0, 0, 0).color(indicatorColor).endVertex();
                builder.vertex(pose, (float) (RADIUS * Math.cos(nextRadius)), 0, (float) (RADIUS * Math.sin(nextRadius))).color(indicatorColor).endVertex();
                builder.vertex(pose, (float) (RADIUS * Math.cos(currentRadius)), 0, (float) (RADIUS * Math.sin(currentRadius))).color(indicatorColor).endVertex();
            }

            // end
            tesselator.end();
            poseStack.popPose();
        }
    }

    private void renderStatus(PoseStack stack, Component component, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(.5f, .5f, .5f);
        stack.mulPose(context.getEntityRenderer().cameraOrientation());
        stack.scale(-.025f, -.025f, .025f);
        Matrix4f matrix4f = stack.last().pose();
        int alpha = (int)(Minecraft.getInstance().options.getBackgroundOpacity(.25f) * 255.f) << 24;
        Font font = context.getFont();
        float f = -font.width(component) / 2.f;
        font.drawInBatch(component, f, 0.f, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, alpha, packedLight);
        font.drawInBatch(component, f, 0.f, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        stack.popPose();
    }
}