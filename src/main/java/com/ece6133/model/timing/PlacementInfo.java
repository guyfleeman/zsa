package com.ece6133.model.timing;

public class PlacementInfo {
    public String name = "";
    public int x = 0;
    public int y = 0;
    public int subblk = 0;
    public int _blknum = 0;

    public float xUm = 0.0f;
    public float yUm = 0.0f;

    public static int rectDistBetween(final PlacementInfo a, final PlacementInfo b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public int restDistTo(final PlacementInfo a) {
        return PlacementInfo.rectDistBetween(this, a);
    }

    @Override
    public String toString() {
        return "PL Info (" + name +"): [" + x + ", " + y + "], " + subblk + ", " + _blknum;
    }
}
