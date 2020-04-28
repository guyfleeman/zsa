package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

/**
 * utility class to help the parser build LUTs
 */
public class LutBuilder {
    protected static LutBuilder instance;

    static {
        instance = new LutBuilder();
    }

    /**
     * gets the singleton instance
     * @return
     */
    public static LutBuilder getInstance() {
        return instance;
    }

    private boolean buildActive = false;
    private Lut lut = null;

    private LutBuilder() {}

    /**
     * resets the interative parse build
     */
    public void resetBuild() {
        buildActive = false;
    }

    /**
     * mark the start of a new instance build
     */
    public void flagStartBuild() {
        lut = new Lut();
        buildActive = true;
    }

    /**
     * checks if a build is active
     * @return
     */
    public boolean isBuildActive() {
        return buildActive;
    }

    /**
     * parse an appended line
     * @param line line
     */
    public void appendDefLine(String line) {
        if (line.matches("[10-]{1,6}\\s[10]")) {
            //TODO load LUT cfg (prob irrel)
            return;
        }

        String[] nets = line.split(" ");
        for (int i = 0; i < nets.length; i++) {
            if (nets[i].equalsIgnoreCase("\\")) {
                return;
            }

            // string is not a line cont and is the last thing... this is the driven net
            // all others are inputs
            if (i == (nets.length - 1)) {
                lut.setOutput(new NetNode(nets[i]));
            } else {
                lut.getInputs().add(new NetNode(nets[i]));
            }
        }
    }

    /**
     * get the constructed instance
     * @return built LUT
     */
    public Lut get() {
        return lut;
    }
}
