package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarsePathList {
    private ArrayList<CoarsePathSegment> coarsePathSegments = new ArrayList<>();

    public ArrayList<CoarsePathSegment> getCoarsePathSegments() {
        return coarsePathSegments;
    }

    public void setCoarsePathSegments(ArrayList<CoarsePathSegment> coarsePathSegments) {
        this.coarsePathSegments = coarsePathSegments;
    }
}
