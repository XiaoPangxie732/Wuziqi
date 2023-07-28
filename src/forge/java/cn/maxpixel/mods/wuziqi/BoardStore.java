package cn.maxpixel.mods.wuziqi;

import net.minecraft.world.phys.Vec2;
import org.apache.http.util.Asserts;

public class BoardStore {

    public final BoardSizeSetting setting;
    private final PieceType[][] store;

    public BoardStore(BoardSizeSetting setting) {
        this.setting = setting;
        this.store = new PieceType[setting.length][setting.length];
        for (int i = 0; i < setting.length; i++) {
            for (int j = 0; j < setting.length; j++) {
                store[i][j] = PieceType.EMPTY;
            }
        }
    }

    private void checkPos(int x, int z) {
        Asserts.check(x >= 1 && x <= setting.length, "x must in 1.." + setting.length);
        Asserts.check(z >= 1 && z <= setting.length, "x must in 1.." + setting.length);
    }

    public PieceType getSlotPiece(int x, int z) {
        checkPos(x, z);
        return store[x - 1][z - 1];
    }

    public PieceType getSlotPiece(Vec2 pos) {
        return getSlotPiece((int) pos.x, (int) pos.y);
    }

    public boolean hasPiece(int x, int z) {
        return getSlotPiece(x, z).isEmpty;
    }

    public boolean hasPiece(Vec2 pos) {
        return getSlotPiece(pos).isEmpty;
    }

    public void setPiece(int x, int z, PieceType piece) {
        checkPos(x, z);
        store[x - 1][z - 1] = piece;
    }

    public void setPiece(Vec2 pos, PieceType piece) {
        setPiece((int) pos.x, (int) pos.y, piece);
    }

}
