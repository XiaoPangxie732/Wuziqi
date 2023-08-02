package cn.maxpixel.mods.wuziqi.client.screen;

import cn.maxpixel.mods.wuziqi.block.entity.BoardBlockEntity;
import net.minecraft.client.Minecraft;

public class Screens {
    public static void openPrepareMatchScreen(BoardBlockEntity blockEntity) {
        Minecraft.getInstance().setScreen(new PrepareMatchScreen(blockEntity));
    }
}