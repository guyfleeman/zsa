package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarsePathList {
    private ArrayList<CoarsePath> coarsePathSegments = new ArrayList<>();

    public ArrayList<CoarsePath> getCoarsePathSegments() {
        return coarsePathSegments;
    }

    public void setCoarsePathSegments(ArrayList<CoarsePath> coarsePathSegments) {
        this.coarsePathSegments = coarsePathSegments;
    }
}
