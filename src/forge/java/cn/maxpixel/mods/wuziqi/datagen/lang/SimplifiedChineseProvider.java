package cn.maxpixel.mods.wuziqi.datagen.lang;

import cn.maxpixel.mods.wuziqi.client.screen.PrepareMatchScreen;
import cn.maxpixel.mods.wuziqi.registry.BlockRegistry;
import cn.maxpixel.mods.wuziqi.registry.CreativeModeTabRegistry;
import cn.maxpixel.mods.wuziqi.util.I18nUtil;
import net.minecraft.data.PackOutput;

public class SimplifiedChineseProvider extends AbstractLangProvider {
    public SimplifiedChineseProvider(PackOutput output) {
        super(output, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addItemGroup(CreativeModeTabRegistry.GENERAL_NAME, "五子棋");

        addBlock(BlockRegistry.OAK_BOARD, "橡木棋盘");

        addPrepareMatchScreen();
        add(I18nUtil.make("block_entity", "board.your_turn"), "您");
        add(I18nUtil.make("block_entity", "board.opponents_turn"), "对手");
        add(I18nUtil.make("block_entity", "board.status"), "轮到 %s 下棋");
        add(I18nUtil.make("block_entity", "board.win.white"), "白方获胜");
        add(I18nUtil.make("block_entity", "board.win.black"), "黑方获胜");
    }

    private void addPrepareMatchScreen() {
        addScreen(PrepareMatchScreen.SCREEN, "wuziqi", "五子棋");
        addScreen(PrepareMatchScreen.SCREEN, "players", "玩家");
        addScreen(PrepareMatchScreen.SCREEN, "join", "加入");
        addScreen(PrepareMatchScreen.SCREEN, "quit", "退出");
        addScreen(PrepareMatchScreen.SCREEN, "start", "开始");
        addScreen(PrepareMatchScreen.SCREEN, "start_sp", "开始(单人)");
    }
}