package cn.maxpixel.mods.wuziqi.board;

import org.jetbrains.annotations.Nullable;

public enum PieceType {
    BLACK((byte) 1), WHITE((byte) 2);
    public static final byte CODE_BLACK = 1;
    public static final byte CODE_WHITE = 2;

    public final byte code;

    PieceType(byte code) {
        this.code = code;
    }

    public static @Nullable PieceType getFromCode(byte code) {
        return switch (code) {
            case CODE_BLACK -> BLACK;
            case CODE_WHITE -> WHITE;
            default -> null;
        };
    }
}