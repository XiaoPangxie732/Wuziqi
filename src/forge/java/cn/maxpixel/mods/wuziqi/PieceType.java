package cn.maxpixel.mods.wuziqi;

public enum PieceType {
    EMPTY(true), BLACK(false), WHITE(false);

    final boolean isEmpty;

    PieceType(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

}