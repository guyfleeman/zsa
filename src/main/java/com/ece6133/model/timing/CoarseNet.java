package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarseNet {
    public String name = "";
    public NetNode source = null;
    public ArrayList<NetNode> sinks = new ArrayList<>();
    public int longestRectDist = -1;
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
