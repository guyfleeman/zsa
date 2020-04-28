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
}
