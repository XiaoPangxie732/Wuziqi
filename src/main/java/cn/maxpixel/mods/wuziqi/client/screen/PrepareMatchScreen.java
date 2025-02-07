package cn.maxpixel.mods.wuziqi.client.screen;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import cn.maxpixel.mods.wuziqi.network.Network;
import cn.maxpixel.mods.wuziqi.network.serverbound.ServerboundPrepareMatchPacket;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class PrepareMatchScreen extends Screen {
    public static final String SCREEN = "prepare_match";
    private static final int WIDTH = 300;
    public static final Component WUZIQI = Component.translatable(I18nUtil.makeScreenText(SCREEN, "wuziqi"));
    public static final Component PLAYERS = Component.translatable(I18nUtil.makeScreenText(SCREEN, "players"));
    public static final Component JOIN = Component.translatable(I18nUtil.makeScreenText(SCREEN, "join"));
    public static final Component QUIT = Component.translatable(I18nUtil.makeScreenText(SCREEN, "quit"));
    public static final Component START = Component.translatable(I18nUtil.makeScreenText(SCREEN, "start"));
    public static final Component START_SINGLEPLAYER = Component.translatable(I18nUtil.makeScreenText(SCREEN, "start_sp"));

    private final BoardBlockEntity blockEntity;

    private PlayerSelectionList queuedPlayers;
    private Button toggleJoin;
    private Button startMatch;

    private boolean joined;

    public PrepareMatchScreen(BoardBlockEntity blockEntity) {
        super(GameNarrator.NO_TITLE);
        this.blockEntity = blockEntity;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, WUZIQI, this.width / 2, 20, 0xFFFFFF);
        graphics.fill(queuedPlayers.getLeft(), queuedPlayers.getTop(), queuedPlayers.getRight(), queuedPlayers.getBottom(), 0xE0101010);// background for queuedPlayers
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        toggleJoin.setMessage(joined ? QUIT : JOIN);
        var playerCount = queuedPlayers.children().size();
        if (playerCount == 1) {
            startMatch.setMessage(START_SINGLEPLAYER);
        } else {
            startMatch.setMessage(START);
        }
        startMatch.active = playerCount >= 1;
    }

    @Override
    protected void init() {
        super.init();
        Network.CHANNEL.sendToServer(new ServerboundPrepareMatchPacket(ServerboundPrepareMatchPacket.Action.SYNC, blockEntity.getBlockPos()));
        this.queuedPlayers = new PlayerSelectionList(minecraft, WIDTH, 40, 40, 160, 20);
        queuedPlayers.setLeftPos(width / 2 - WIDTH / 2);
        queuedPlayers.setRenderBackground(false);
        queuedPlayers.setRenderTopAndBottom(false);
        queuedPlayers.setRenderHeader(true, 20);
        addRenderableWidget(queuedPlayers);

        this.toggleJoin = Button.builder(JOIN, button -> {
            if (joined) {
                joined = false;
                Network.CHANNEL.sendToServer(new ServerboundPrepareMatchPacket(ServerboundPrepareMatchPacket.Action.QUIT, blockEntity.getBlockPos()));
            } else {
                joined = true;
                Network.CHANNEL.sendToServer(new ServerboundPrepareMatchPacket(ServerboundPrepareMatchPacket.Action.JOIN, blockEntity.getBlockPos()));
            }
        }).pos(width / 2 - 150 - 1, 170).build();
        addRenderableWidget(toggleJoin);

        this.startMatch = Button.builder(START, button -> {
            Network.CHANNEL.sendToServer(new ServerboundPrepareMatchPacket(ServerboundPrepareMatchPacket.Action.START, blockEntity.getBlockPos()));
            onClose();
        }).pos(width / 2 + 1, 170).build();
        addRenderableWidget(startMatch);
    }

    public void setQueuedPlayers(UUID[] players) {
        var list = queuedPlayers.children();
        list.clear();
        for (UUID uuid : players) {
            if (minecraft.level.getPlayerByUUID(uuid) instanceof AbstractClientPlayer player) {
                if (player == minecraft.player) joined = true;
                list.add(queuedPlayers.new Entry(player, minecraft));
            }
        }
    }
}