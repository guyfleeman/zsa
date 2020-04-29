package com.ece6133.model.timing;

import java.util.ArrayList;

import java.util.Collections;

/**
 * contains the zero slack algorithm impl
 */
public class ZeroSlackTimingModel {

    public static void prepInitialArrivalTimes(K6DesignModel dm) {
        computeInitialArrivalTimes(dm);
    }

    public static void prepRequiredArrivalTimes(K6DesignModel dm) {
        computeRequiredArrivalTimes(dm);
    }

    public static void prepEdgeSlacks(K6DesignModel dm) {
        computeEdgeSlacks(dm);
    }

    /**
     * runs the zero slack algorithms on a design
     * @param dm
     */
    public static void zeroSlack(K6DesignModel dm) {
        computeInitialArrivalTimes(dm);
        computeRequiredArrivalTimes(dm);
        computeEdgeSlacks(dm);

        while (hasNodeWithPositiveSlack(dm)) {
            BlockNode leastPosSlack = getNodeWithLeastPositiveSlack(dm);
            ArrayList<CoarsePathSegment> fwdSegs = getFwdSegments(leastPosSlack, dm);
            ArrayList<CoarsePathSegment> bakSegs = getRevSegments(leastPosSlack, dm);
            int fwdAdj = bakSegs.isEmpty() ? leastPosSlack.getSlack() : (int) Math.ceil((float) leastPosSlack.getSlack() / 2.0f);
            int bakAdj = fwdSegs.isEmpty() ? leastPosSlack.getSlack() : (int) Math.floor((float) leastPosSlack.getSlack() / 2.0f);
            for (CoarsePathSegment cps: fwdSegs) {
                if (cps.isFinalized()) {
                    continue;
                }

                cps.setSlack(cps.getSlack() - fwdAdj);
                BlockNode sink = dm.luBlkNode(cps.getSinkBlock());
                if (sink.getSlack() > 0) {
                    sink.setSlack(sink.getSlack() - fwdAdj);
                }
            }

            for (CoarsePathSegment cps: bakSegs) {
                if (cps.isFinalized()) {
                    continue;
                }

                cps.setSlack(cps.getSlack() - bakAdj);
            }

            leastPosSlack.setSlack(0);
            assignEdgeDeltas(dm);
        }
    }

    /**
     * computes the initial arrival times for the zero slack algorithm
     * @param dm
     */
    public static void computeInitialArrivalTimes(K6DesignModel dm) {
        ArrayList<BlockNode> globalSources = new ArrayList<>();
        ArrayList<BlockNode> globalSinks = new ArrayList<>();
        for (BlockNode b: dm.getCoarsePathList().getTimingGraphNodes().values()) {
            if (b.isSource()) {
                globalSources.add(b);
            } else if (b.isSink()) {
                globalSinks.add(b);
            }
        }

        for (BlockNode b: globalSources) {
            for (CoarsePath cp: b.getDownstreamPaths()) {
                for (CoarsePathSegment cps: cp.getPathSegments()) {
                    BlockNode curNode = dm.getCoarsePathList().getTimingGraphNodes().get(cps.getSourceBlock());
                    BlockNode nextNode = dm.getCoarsePathList().getTimingGraphNodes().get(cps.getSinkBlock());
                    int segDelayContrib = (int) cps.getDelay();
                    int curPathAT = curNode.getArrivalTime() + segDelayContrib;
                    int curNextNodeAT = nextNode.getArrivalTime();
                    if (curPathAT > curNextNodeAT) {
                        nextNode.setArrivalTime(curPathAT);
                    }
                }
            }
        }

        int largestSinkAT = 0;
        for (BlockNode sink: globalSinks) {
            if (sink.getArrivalTime() > largestSinkAT) {
                largestSinkAT = sink.getArrivalTime();
            }
        }

        for (BlockNode sink: globalSinks) {
            sink.setRequiredArrivalTime(largestSinkAT);
        }
    }

    /**
     * computes the requires arrival times for the zero slack algorithm
     * @param dm
     */
    public static void computeRequiredArrivalTimes(K6DesignModel dm) {
        for (CoarsePath cp: dm.getCoarsePathList().getCoarsePaths()) {
            ArrayList<CoarsePathSegment> revPath = new ArrayList<>(cp.getPathSegments());
            Collections.reverse(revPath);
            for (CoarsePathSegment cps: revPath) {
                BlockNode curNode = dm.getCoarsePathList().getTimingGraphNodes().get(cps.getSinkBlock());
                BlockNode nextNode = dm.getCoarsePathList().getTimingGraphNodes().get(cps.getSourceBlock());
                int segDelayContrib = (int) cps.getDelay();
                int curPathReqAT = curNode.getRequiredArrivalTime() - segDelayContrib;
                int curNextNodeReqAT = nextNode.getRequiredArrivalTime();
                if (curPathReqAT < curNextNodeReqAT) {
                    nextNode.setRequiredArrivalTime(curPathReqAT);
                }
            }
        }
    }

    /**
     * computes the edge slacks for the zero slack algorithm
     * @param dm
     */
    public static void computeEdgeSlacks(K6DesignModel dm) {
        for (CoarsePathSegment cps: dm.getCoarsePathList().getCoarsePathSegments()) {
            cps.setSlack(dm.luBlkNode(cps.getSinkBlock()).getRequiredArrivalTime()
                    - dm.luBlkNode(cps.getSourceBlock()).getArrivalTime()
                    - cps.getDelay());
        }
    }

    /**
     * checks if a node still has positive slack
     * @param dm
     * @return
     */
    public static boolean hasNodeWithPositiveSlack(K6DesignModel dm) {
        for (BlockNode b: dm.getCoarsePathList().getTimingGraphNodes().values()) {
            if (b.getSlack() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * gets the node with the least positive slack
     * @param dm
     * @return
     */
    public static BlockNode getNodeWithLeastPositiveSlack(K6DesignModel dm) {
        BlockNode curLowestNode = null;
        for (BlockNode b: dm.getCoarsePathList().getTimingGraphNodes().values()) {
            if (b.getSlack() > 0) {
                if (curLowestNode == null || b.getSlack() < curLowestNode.getSlack()) {
                    curLowestNode = b;
                }
            }
        }

        return curLowestNode;
    }

    /**
     * gets the forward segments for a given node for the zero slack algorithm
     * @param b
     * @param dm
     * @return
     */
    public static ArrayList<CoarsePathSegment> getFwdSegments(BlockNode b, K6DesignModel dm) {
        ArrayList<CoarsePathSegment> fwdSegs = new ArrayList<>();
        for (CoarsePathSegment cps: dm.getCoarsePathList().getCoarsePathSegments()) {
            if (cps.isFinalized() || cps.getSlack() == 0) {
                continue;
            }

            if (dm.luBlkNode(cps.getSourceBlock()) == b) {
                fwdSegs.add(cps);
                fwdSegs.addAll(getFwdSegments(dm.luBlkNode(cps.getSinkBlock()), dm));
            }
        }

        return fwdSegs;
    }

    /**
     * gets the backward segments for a given node for the zero slack algorithm
     * @param b
     * @param dm
     * @return
     */
    public static ArrayList<CoarsePathSegment> getRevSegments(BlockNode b, K6DesignModel dm) {
        ArrayList<CoarsePathSegment> fwdSegs = new ArrayList<>();
        for (CoarsePathSegment cps: dm.getCoarsePathList().getCoarsePathSegments()) {
            if (dm.luBlkNode(cps.getSinkBlock()) == b) {
                fwdSegs.add(cps);
            }
        }

        return fwdSegs;
    }

    /**
     * assigns edge deltas for the zero slack algorithm
     * @param dm
     */
    public static void assignEdgeDeltas(K6DesignModel dm) {
        for (CoarsePathSegment cps: dm.getCoarsePathList().getCoarsePathSegments()) {
            if (dm.luBlkNode(cps.getSourceBlock()).getSlack() == 0
                    && dm.luBlkNode(cps.getSinkBlock()).getSlack() == 0
                    && !cps.isFinalized()) {
                cps.setDelta(cps.getSlack());
                cps.setSlack(0);
                cps.finalizeSlack();
                //System.out.println("set delta " + cps.getDelta() + " for " + cps);
            }
        }
    }
}
