package com.ece6133.model.timing;

import java.util.ArrayList;

/**
 * represents a coarse net
 *
 * coarse nets have timing estimates, not exact timing based on explicit routing
 */
public class CoarseNet {
    /**
     * net name
     */
    public String name = "";

    /**
     * net node/port that drives the net, the source
     */
    public NetNode source = null;

    /**
     * net nodes/ports driven by the net, the sinks
     */
    public ArrayList<NetNode> sinks = new ArrayList<>();

    /**
     * longest rectilinear distance between the source and any sink
     */
    public int longestRectDist = -1;

    /**
     * longest calculated delay estimate from source to a sink
     */
    public float longestDelay = -1.0f;

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(name).append(": ");
        ret.append("Source: <").append(source).append(">, ").append("\r\n\t");
        ret.append("Sinks: [<");
        for (NetNode sink: sinks) {
            ret.append(sink).append(">, <");
        }
        ret.append(">]").append("\r\n");
        return ret.toString();
    }
}
