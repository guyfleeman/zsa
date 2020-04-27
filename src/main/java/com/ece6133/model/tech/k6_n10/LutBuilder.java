package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

public class LutBuilder {
    protected static LutBuilder instance;

    static {
        instance = new LutBuilder();
    }

    public static LutBuilder getInstance() {
        return instance;
    }

    private boolean buildActive = false;
    private Lut lut = null;

    private LutBuilder() {}

    public void resetBuild() {
        buildActive = false;
    }

    public void flagStartBuild() {
        lut = new Lut();
        buildActive = true;
    }

    public boolean isBuildActive() {
        return buildActive;
    }

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

    public Lut get() {
        return lut;
    }
}
