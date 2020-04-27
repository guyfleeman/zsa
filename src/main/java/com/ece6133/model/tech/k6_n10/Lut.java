package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

import java.util.ArrayList;

public class Lut {
    private ArrayList<NetNode> inputs = new ArrayList<>();
    private NetNode output;

    public Lut() {}

    public ArrayList<NetNode> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<NetNode> inputs) {
        this.inputs = inputs;
    }

    public NetNode getOutput() {
        return output;
    }

    public void setOutput(NetNode output) {
        this.output = output;
    }
}
