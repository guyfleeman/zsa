package com.ece6133.model.timing;

import java.util.ArrayList;

/**
 * a coarse path is one path of a net tree (e.g. single sink) and again has a timing estimate
 * not an exact timing from completed routing
 */
public class CoarsePath {
    private ArrayList<CoarsePathSegment> pathSegments = new ArrayList<>();

    /**
     * returns the segments that comprise a gated path
     * @return segments
     */
    public ArrayList<CoarsePathSegment> getPathSegments() {
        return pathSegments;
    }

    /**
     * sets the backing segments
     * @param pathSegments
     */
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
