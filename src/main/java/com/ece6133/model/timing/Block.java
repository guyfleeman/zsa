package com.ece6133.model.timing;

import com.ece6133.model.tech.k6_n10.PlacementInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class Block {
    private String name = "";
    private HashMap<String, ArrayList<String>> inputs = new HashMap<>();
    private HashMap<String, ArrayList<String>> outputs = new HashMap<>();
    private PlacementInfo placementInfo = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, ArrayList<String>> getInputs() {
        return inputs;
    }

    public void setInputs(HashMap<String, ArrayList<String>> inputs) {
        this.inputs = inputs;
    }

    public HashMap<String, ArrayList<String>> getOutputs() {
        return outputs;
    }

    public void setOutputs(HashMap<String, ArrayList<String>> outputs) {
        this.outputs = outputs;
    }

    public PlacementInfo getPlacementInfo() {
        return placementInfo;
    }

    public void setPlacementInfo(PlacementInfo placementInfo) {
        this.placementInfo = placementInfo;
    }

    @Override
    public String toString() {
        return "Block (" + name + "): " + "I: " + inputs.size() + ", O: " + outputs.size() + ", PlInfo: " + placementInfo;
    }
}
