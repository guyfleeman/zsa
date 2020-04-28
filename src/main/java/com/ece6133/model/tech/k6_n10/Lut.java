package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

import java.util.ArrayList;

/**
 * represents a lut
 */
public class Lut {
    private ArrayList<NetNode> inputs = new ArrayList<>();
    private NetNode output;

    public Lut() {}

    /**
     *
     * @return
     */
    public ArrayList<NetNode> getInputs() {
        return inputs;
    }

    /**
     *
     * @param inputs
     */
    public void setInputs(ArrayList<NetNode> inputs) {
        this.inputs = inputs;
    }

    /**
     *
     * @return
     */
    public NetNode getOutput() {
        return output;
    }

    /**
     *
     * @param output
     */
    public void setOutput(NetNode output) {
        this.output = output;
    }
}
