package com.ece6133.model.timing;

/**
 * data class containing the placement info a block
 */
public class PlacementInfo {
    /**
     * physical tile name
     *
     * differs from the block/tile name for multi tile macros
     */
    public String name = "";

    /**
     * x coord in device units
     */
    public int x = 0;

    /**
     * y coord in device units
     */
    public int y = 0;

    /**
     * subblk for MT macros
     */
    public int subblk = 0;

    /**
     * blknum, architecturally irrelevant but used for debugging and logs
     */
    public int _blknum = 0;

    public float xUm = 0.0f;
    public float yUm = 0.0f;

    /**
     * calculate the rectilinear distance between tiles
     * @param a left
     * @param b right
     * @return dist
     */
    public static int rectDistBetween(final PlacementInfo a, final PlacementInfo b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * calculate the rectilinear distance to another placement
     * @param a dest
     * @return dist
     */
    public int restDistTo(final PlacementInfo a) {
        return PlacementInfo.rectDistBetween(this, a);
    }

    @Override
    public String toString() {
        return "PL Info (" + name +"): [" + x + ", " + y + "], " + subblk + ", " + _blknum;
    }
}
