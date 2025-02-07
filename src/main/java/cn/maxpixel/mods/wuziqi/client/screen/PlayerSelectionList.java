package cn.maxpixel.mods.wuziqi.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerModelPart;

public class PlayerSelectionList extends ObjectSelectionList<PlayerSelectionList.Entry> {
    private final int playersLength = minecraft.font.width(PrepareMatchScreen.PLAYERS);

    public PlayerSelectionList(Minecraft mc, int width, int height, int contentBegin, int contentEnd, int itemHeight) {
        super(mc, width, height, contentBegin, contentEnd, itemHeight);
    }

    @Override
    public void setRenderHeader(boolean renderHeader, int headerHeight) {
        super.setRenderHeader(renderHeader, headerHeight);
    }

    @Override
    public int getRowWidth() {
        return 290;
    }

    @Override
    protected int getScrollbarPosition() {
        return x0 + width;
    }

    @Override
    protected void renderHeader(GuiGraphics graphics, int left, int height) {
        graphics.drawString(minecraft.font, PrepareMatchScreen.PLAYERS, x0 + width / 2 - playersLength / 2, height, 0xFFFFFF, false);
    }

    public class Entry extends ObjectSelectionList.Entry<Entry> {
        private final AbstractClientPlayer player;
        private final Minecraft mc;

        public Entry(AbstractClientPlayer player, Minecraft mc) {
            this.player = player;
            this.mc = mc;
        }

        @Override
        public Component getNarration() {
            return player.getName();
        }

        @Override
        public boolean mouseClicked(double p_94737_, double p_94738_, int p_94739_) {
            setSelected(this);
            return false;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int topPos, int leftPos, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
            PlayerFaceRenderer.draw(graphics, player.getSkinTextureLocation(), leftPos, topPos, 16, player.isModelPartShown(PlayerModelPart.HAT),
                    LivingEntityRenderer.isEntityUpsideDown(player));
            graphics.drawString(mc.font, player.getDisplayName(), leftPos + 16 + 5, topPos + mc.font.lineHeight / 2, 0xFFFFFF);
        }
    }
}