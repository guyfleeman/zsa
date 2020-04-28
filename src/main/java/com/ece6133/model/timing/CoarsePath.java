package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarsePath {
    private ArrayList<CoarsePathSegment> pathSegments = new ArrayList<>();

    public ArrayList<CoarsePathSegment> getPathSegments() {
        return pathSegments;
    }

    public void setPathSegments(ArrayList<CoarsePathSegment> pathSegments) {
        this.pathSegments = pathSegments;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (CoarsePathSegment cps: pathSegments) {
            ret.append(cps).append(", ");
        }
        return ret.toString();
    }
}
