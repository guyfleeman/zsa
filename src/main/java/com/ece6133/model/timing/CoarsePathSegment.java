package com.ece6133.model.timing;

/**
 * a single segment of a path representing a point to point connection between a source and sink, where the source
 * block may be gated or non gated
 */
public class CoarsePathSegment {
    private NetNode source;
    private NetNode sink;
    private int rlDist = 0;
    private int delay = 0;
    private int slack = 0;
    private int delta = 0;
    private boolean finalized = false;

    /**
     * get the block driving the segment
     * @return block
     */
    public Block getSourceBlock() {
        return source.getParent();
    }

    /**
     * get the block driven by the segment
     * @return
     */
    public Block getSinkBlock() {
        return sink.getParent();
    }

    /**
     *
     * @return
     */
    public NetNode getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(NetNode source) {
        this.source = source;
    }

    /**
     *
     * @return
     */
    public NetNode getSink() {
        return sink;
    }

    /**
     *
     * @param sink
     */
    public void setSink(NetNode sink) {
        this.sink = sink;
    }

    /**
     * gets the rectiliniear distance between source and sink for the path
     * @return distance
     */
    public int getRlDist() {
        return rlDist;
    }

    /**
     * sets the rectilinear distance between the source and the sink for the path
     * @param rlDist distance
     */
    public void setRlDist(int rlDist) {
        this.rlDist = rlDist;
    }

    /**
     * get the RC delay estimate for the segment
     * @return
     */
    public int getDelay() {
        return delay;
    }

    /**
     * set the RC delay estimate for the segment
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "" + source.getParent().getName() + "->" + sink.getParent().getName();
    }

    public int getSlack() {
        return slack;
    }

    public void setSlack(int slack) {
        this.slack = slack;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public void finalizeSlack() {
        this.finalized = true;
    }

    public boolean isFinalized() {
        return finalized;
    }
}
