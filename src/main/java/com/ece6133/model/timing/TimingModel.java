package com.ece6133.model.timing;

public class TimingModel {

    public static void greedyZeroSlack() {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        CoarsePathList cpl = K6DesignModelLoader.recoverPaths(cnl, dm.getBlocks());

        HashMap<String, Block> blocks = dm.getBlocks();
        String[] blockNames = dm.getBlockNames();

        int slackMin = Integer.MAX_VALUE;
        int numComponents = dm.getNumBlocks();

        int i = 0;
        while (i < numComponents) {
            computeSlacks(dm, cpl);

            while (i < numComponents && dm.getSlack(blockNames[i]) == 0) {
                i++;
            }

            if (i < numComponents) {
                dm.addSlackDelay(blockNames[i]); // Add delay to block equivalent to block's slack
            }
        }
    }

    public static void computeSlacks(K6DesignModel dm, CoarsePathList cpl) {
        ArrayList<CoarsePath> coarsePaths = cpl.getCoarsePaths();
        for (CoarsePath path : coarsePaths) {
            ArrayList<CoarsePathSegment> segments = path.getPathSegments();

            // Get data ready time
            for (CoarsePathSegment segment : segments) {
                Block source = segment.getSourceBlock();
                Block sink = segment.getSinkBlock();

                int sinkReadyTime = sink.getReadyTime();
                int sourceReadyTime = source.getReadyTime();
                if (sourceReadyTime + source.getDelay() > sinkReadyTime)
                    sink.setReadyTime(sourceReadyTime + source.getDelay());
            }

            // Get data required time
            ListIterator li = segments.listIterator(segments.size());
            while (li.hasPrevious()) {
                Block source = segment.getSourceBlock();
                Block sink = segment.getSinkBlock();

                int sourceRequiredTime = source.getRequiredTime();
                int sinkRequiredTime = sink.getRequiredTime();
                if (sinkRequiredTime - source.getDelay() < sourceRequiredTime)
                    source.setRequiredTime(sinkRequiredTime - source.getDelay());
            }
        }

        // Compute slacks (not very efficient but for testing now)
        for (String block : dm.getBlocks().keySet()) {
            dm.getBlocks().get(block).setSlack();
        }
    }

/*
    public static void zeroSlack() {
        int num = getNumComponents();
        do {
            computeSlacks();
            int slackMin = Integer.MAX_VALUE;
            Node minNode = new Node();
            getMinPositiveSlack();

            for (int i = 0; i < numComponents; i++) {
                if (slack(nodeI) < slackMin && slack(nodeI) > 0) {
                    slackMin = slack(nodeI);
                    minNode = nodeI;
                }
            }

            if (slackMin != Integer.MAX_VALUE) {
                Node a0 = minNode;
                int v = 0;
                // Find forward path segment
                while (x != null) {
                    // find output called node X where trequired(x) = trequired(a) + delay(x) and tactual(x) = tactual(a) + delay(x)
                    if (output != null) {
                        a0 = x; 
                        v++;
                    }
                }

                // Find backward path segment
                int u = 0;
                while (x != null) {
                    // find input called node X where trequired(x) = trequired(a) - delay(x) and tactual(x) = tactual(a) - delay(x)
                    if (output != null) {
                        a0 = x; 
                        u--;
                    }
                }

                // Distribute slacks
                int slack = slackMin;
                for (int i = u; i < v; i++) {
                    int slackHat = slack / (v - i + 1);
                    delay(nodeI) += slackHat;
                    slack -= slackHat;
                }
            }


        } while (slackMin != Integer.MAX_VALUE)
    } */

}
