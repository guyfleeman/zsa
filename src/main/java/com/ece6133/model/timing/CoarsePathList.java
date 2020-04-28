package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarsePathList {
    private ArrayList<CoarsePath> coarsePaths = new ArrayList<>();

    public ArrayList<CoarsePath> getCoarsePaths() {
        return coarsePaths;
    }

    public void setCoarsePaths(ArrayList<CoarsePath> coarsePaths) {
        this.coarsePaths = coarsePaths;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Coarse Path List: (ct:").append(coarsePaths.size()).append(")\r\n");
        for (CoarsePath cp: coarsePaths) {
            ret.append(cp).append("\r\n");
        }
        ret.append("\r\n");
        return ret.toString();
    }
}
