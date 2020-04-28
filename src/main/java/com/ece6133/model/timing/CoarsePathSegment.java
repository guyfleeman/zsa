package com.ece6133.model.timing;

public class CoarsePathSegment {
    private NetNode source;
    private NetNode sink;
    private int rlDist = 0;
    private float rcRteDelay = 0;

    public Block getSourceBlock() {
        return source.getParent();
    }

    public Block getSinkBlock() {
        return sink.getParent();
    }

    public NetNode getSource() {
        return source;
    }

    public void setSource(NetNode source) {
        this.source = source;
    }

    public NetNode getSink() {
        return sink;
    }

    public void setSink(NetNode sink) {
        this.sink = sink;
    }

    public int getRlDist() {
        return rlDist;
    }

    public void setRlDist(int rlDist) {
        this.rlDist = rlDist;
    }

    public float getRcRteDelay() {
        return rcRteDelay;
    }

    public void setRcRteDelay(float rcRteDelay) {
        this.rcRteDelay = rcRteDelay;
    }

    @Override
    public String toString() {
        return "" + source.getParent().getName() + " -> " + sink.getParent().getName() + "[TODO DLY]";
    }
}
