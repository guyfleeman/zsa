package com.ece6133.model.timing;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * list of coarse paths. A wrapper for printing, debugging, and type management
 */
public class CoarsePathList {
    private ArrayList<CoarsePath> coarsePaths = new ArrayList<>();
    private ArrayList<CoarsePathSegment> coarsePathSegments = new ArrayList<>();
    private HashMap<Block, BlockNode> timingGraphNodes = new HashMap<>();

    /**
     *
     * @return
     */
    public ArrayList<CoarsePath> getCoarsePaths() {
        return coarsePaths;
    }

    /**
     *
     * @param coarsePaths
     */
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

    public ArrayList<CoarsePathSegment> getCoarsePathSegments() {
        return coarsePathSegments;
    }

    public void setCoarsePathSegments(ArrayList<CoarsePathSegment> coarsePathSegments) {
        this.coarsePathSegments = coarsePathSegments;
    }

    public HashMap<Block, BlockNode> getTimingGraphNodes() {
        return timingGraphNodes;
    }

    public void setTimingGraphNodes(HashMap<Block, BlockNode> timingGraphNodes) {
        this.timingGraphNodes = timingGraphNodes;
    }
}
