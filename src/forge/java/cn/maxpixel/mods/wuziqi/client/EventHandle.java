package cn.maxpixel.mods.wuziqi.client;

import cn.maxpixel.mods.wuziqi.BoardSizeSetting;
import cn.maxpixel.mods.wuziqi.WuziqiMod;
import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = WuziqiMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandle {
    private static final int FRAGMENTS = 32;
    private static final int COLOR = Integer.MAX_VALUE;
    private static final float RADIUS = 1.0f / 16 / 2;

    @SubscribeEvent
    public static void renderIndicator(RenderLevelStageEvent event) {
        if (event.getStage() == (RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES)) {
            //get player
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            //get level
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            //get hit pos and hit block
            var hitResult = player.pick(player.getEntityReach(), event.getPartialTick(), false);
            if (hitResult.getType() != HitResult.Type.BLOCK) return;
            var hitLocation = hitResult.getLocation();
            var blockPos = ((BlockHitResult)hitResult).getBlockPos();
            var block = level.getBlockState(new BlockPos(blockPos)).getBlock();
            if (block != BlockRegistry.OAK_BOARD.get()) return;

            if (level.getBlockEntity(blockPos) instanceof BoardBlockEntity blockEntity) {
                //get draw base
                var locationHitLocation = hitLocation.subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                BoardSizeSetting setting = blockEntity.store.setting;
                if (!setting.checkHitLocation(locationHitLocation)) return;
                var slotPos = setting.calculatePos(locationHitLocation);
                if (blockEntity.store.hasPiece(slotPos)) return;
                Vec2 drawBase = setting.calculatePercentPos(slotPos);

                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();
                var cameraPos = event.getCamera().getPosition();

                //translate to world zero
                poseStack.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);

                //translate to draw zero
                poseStack.translate(blockPos.getX() + drawBase.x,
                        blockPos.getY() + (setting.height * 1.0 / BoardSizeSetting.BLOCK_LENGTH) + 0.02f,
                        blockPos.getZ() + drawBase.y);
                Tesselator tesselator = Tesselator.getInstance();

                //draw begin
                BufferBuilder builder = tesselator.getBuilder();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                builder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

                //fill vertex
                float eachRadius = (float) (Math.PI * 2 / FRAGMENTS);
                var pose = poseStack.last().pose();
                for (int i = 0; i < FRAGMENTS; i++) {
                    var currentRadius = eachRadius * i;
                    var nextRadius = eachRadius * (i + 1);
                    builder.vertex(pose, 0, 0, 0).color(COLOR).endVertex();
                    builder.vertex(pose, (float) (RADIUS * Math.cos(nextRadius)), 0, (float) (RADIUS * Math.sin(nextRadius))).color(COLOR).endVertex();
                    builder.vertex(pose, (float) (RADIUS * Math.cos(currentRadius)), 0, (float) (RADIUS * Math.sin(currentRadius))).color(COLOR).endVertex();
                }

                //end
                tesselator.end();
                poseStack.popPose();
            }
        }
    }
}
