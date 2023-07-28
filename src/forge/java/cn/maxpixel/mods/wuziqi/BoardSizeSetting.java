package cn.maxpixel.mods.wuziqi;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.http.util.Asserts;

public final class BoardSizeSetting {

    public static final int BLOCK_LENGTH = 16;

    public static final BoardSizeSetting SETTING = BoardSizeSetting.of(1, 14);

    public final int height;
    public final int length;
    public final int min;
    public final int max;

    private BoardSizeSetting(int height, int length) {
        this.height = height;
        this.length = length;
        this.min = (BLOCK_LENGTH - length) / 2;
        this.max = (BLOCK_LENGTH + length) / 2;
    }

    public static BoardSizeSetting of(int height, int length) {
        Asserts.check(height >= 1 && height <= 16, "height should in 1..16 instead of " + height);
        Asserts.check(length >= 2 && length <= 16, "length should in 1..16 instead of " + length);
        return new BoardSizeSetting(height, length);
    }

    public boolean checkHitLocation(Vec3 hitLocation) {
        return checkHitLocationHeight(hitLocation) && checkPercentPosInRange(hitLocation.x) && checkPercentPosInRange(hitLocation.z);
    }

    public boolean checkHitLocationHeight(Vec3 hitLocation) {
        return hitLocation.y == (height * 1.0 / BLOCK_LENGTH);
    }

    public BoardStore makeBoard() {
        return new BoardStore(SETTING);
    }

    public boolean checkPercentPosInRange(double value) {
        return value >= (min * 1.0 / BLOCK_LENGTH) && value <= (max * 1.0 / BLOCK_LENGTH);
    }

    private void assertInLengthRange(double value) {
        Asserts.check(checkPercentPosInRange(value), "");
    }

    /**
     * @param locationHitLocation x and z must range in {@link #min}/{@link #BLOCK_LENGTH}..{@link #max}/{@link #BLOCK_LENGTH}
     * @return pos range in 1..{@link #length}
     */
    public Vec2 calculatePos(Vec3 locationHitLocation) {
        assertInLengthRange(locationHitLocation.x);
        assertInLengthRange(locationHitLocation.z);
        var x = locationHitLocation.x;
        var z = locationHitLocation.z;
        double range = ((max * 1.0) / BLOCK_LENGTH) - ((min * 1.0) / BLOCK_LENGTH);
        var normalizedX = (x - (min * 1.0) / BLOCK_LENGTH) / range;
        var normalizedZ = (z - (min * 1.0) / BLOCK_LENGTH) / range;
        var finalX = (int) Math.round(normalizedX * (length - 1) + 1);
        var finalZ = (int) Math.round(normalizedZ * (length - 1) + 1);

        Asserts.check(finalX >= 1 && finalX <= length, "x must range in 1.." + length + " instead of " + finalX);
        Asserts.check(finalZ >= 1 && finalZ <= length, "z must range in 1.." + length + "instead of " + finalZ);

        return new Vec2(finalX, finalZ);
    }

    public Vec2 calculatePercentPos(int x, int z) {
        var normalizedX = (x - 1) * 1.0 / (length - 1);
        var normalizedZ = (z - 1) * 1.0 / (length - 1);
        double range = ((max * 1.0) / BLOCK_LENGTH) - ((min * 1.0) / BLOCK_LENGTH);
        var posX = normalizedX * range + ((min * 1.0) / BLOCK_LENGTH);
        var posZ = normalizedZ * range + ((min * 1.0) / BLOCK_LENGTH);

        return new Vec2((float) posX, (float) posZ);
    }

    public Vec2 calculatePercentPos(Vec2 hitPos) {
        return calculatePercentPos((int) hitPos.x, (int) hitPos.y);
    }
}
