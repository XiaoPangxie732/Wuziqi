package cn.maxpixel.mods.wuziqi.board.wuziqi;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Board {
    public static final String PIECES_KEY = "Pieces";
    public static final int BOARD_SIZE = 15;

    private final Runnable setChanged;
    private final PieceType[][] pieces;

    public Board(Runnable setChanged) {
        this(setChanged, new PieceType[BOARD_SIZE][BOARD_SIZE]);
    }

    public Board(Runnable setChanged, PieceType[][] pieces) {
        this.setChanged = setChanged;
        this.pieces = Objects.requireNonNull(pieces);
    }

    private boolean checkPieceBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public boolean hasPiece(int x, int y) {
        return checkPieceBounds(x, y) && pieces[x][y] != null;
    }

    public boolean putPiece(int x, int y, @Nullable PieceType type) {
        if (hasPiece(x, y) || !checkPieceBounds(x, y)) return false;
        pieces[x][y] = type;
        setChanged.run();
        return true;
    }

    public @Nullable PieceType getPiece(int x, int y) {
        return checkPieceBounds(x, y) ? pieces[x][y] : null;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        byte[] pieceArr = new byte[BOARD_SIZE * BOARD_SIZE];
        int i = 0;
        for (PieceType[] piece : pieces) {
            for (PieceType p : piece) {
                pieceArr[i++] = p == null ? 0 : p.code;
            }
        }
        tag.putByteArray(PIECES_KEY, pieceArr);

        return tag;
    }

    public static Board load(Runnable setChanged, CompoundTag tag) {
        PieceType[][] pieces = new PieceType[BOARD_SIZE][BOARD_SIZE];
        if (tag.contains(PIECES_KEY, Tag.TAG_BYTE_ARRAY)) {
            byte[] arr = tag.getByteArray(PIECES_KEY);
            for (int i = 0; i < arr.length; i++) {
                var type = switch (arr[i]) {
                    case PieceType.CODE_BLACK -> PieceType.BLACK;
                    case PieceType.CODE_WHITE -> PieceType.WHITE;
                    default -> null;
                };
                pieces[i / BOARD_SIZE][i % BOARD_SIZE] = type;
            }
        }
        return new Board(setChanged, pieces);
    }
}