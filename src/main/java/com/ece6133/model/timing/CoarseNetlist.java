package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarseNetlist {
    private ArrayList<CoarseNet> nets = new ArrayList<>();

    public CoarseNetlist() {}

    public ArrayList<CoarseNet> getNets() {
        return nets;
    }

    public void setNets(ArrayList<CoarseNet> nets) {
        this.nets = nets;
    }

    public void addCoarseNet(CoarseNet net) {
        this.getNets().add(net);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (CoarseNet cn: nets) {
            ret.append(cn.name).append(": ");
            ret.append("Source: <").append(cn.source).append(">, ").append("\r\n\t");
            ret.append("Sinks: [<");
            for (NetNode sink: cn.sinks) {
                ret.append(sink).append(">, <");
            }
            ret.append(">]").append("\r\n");
            ret.append("\r\n");
        }

        return ret.toString();
    }
}
