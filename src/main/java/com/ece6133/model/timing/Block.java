package com.ece6133.model.timing;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * represent a tile and it's configuration in the K6 architecture
 */
public class Block {
    private String name = "";
    private HashMap<String, ArrayList<String>> inputs = new HashMap<>();
    private HashMap<String, ArrayList<String>> outputs = new HashMap<>();
    private PlacementInfo placementInfo = null;
    private boolean gated = false;
    private int slack = 0;
    private int delay = 0;
    private int requiredTime = 0;
    private int readyTime = 0;

    /**
     * gets the block name as defined by the arch
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * gets the delay
     * @return
     */
    public int getDelay() {
        return delay;
    }

    /**
     * gets the slack
     * @return
     */
    public int getSlack() {
        return slack;
    }

    /**
     * gets the ready time
     * @return
     */
    public int getReadyTime() {
        return readyTime;
    }

    /**
     * gets the required time
     * @return
     */
    public int getRequiredTime() {
        return requiredTime;
    }

    /**
     * sets the name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the delay
     * @param delay delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * adds delay
     * @param delay delay
     */
    public void addDelay(int delay) {
        this.delay += delay;
    }

    /**
     * sets the slack
     */
    public void setSlack() {
        this.slack = this.readyTime - this.requiredTime;
    }

    /**
     * sets the readyTime
     * @param readyTime ready time
     */
    public void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }

    /**
     * sets the requiredTime
     * @param requiredTime required time
     */
    public void setRequiredTime(int requiredTime) {
        this.requiredTime = requiredTime;
    }

    /**
     * get all input net names
     * @return names
     */
    public HashMap<String, ArrayList<String>> getInputs() {
        return inputs;
    }

    /**
     * sets input net names
     * @param inputs names
     */
    public void setInputs(HashMap<String, ArrayList<String>> inputs) {
        this.inputs = inputs;
    }

    /**
     * gets output net names
     * @return names
     */
    public HashMap<String, ArrayList<String>> getOutputs() {
        return outputs;
    }

    /**
     * sets output net names
     * @param outputs names
     */
    public void setOutputs(HashMap<String, ArrayList<String>> outputs) {
        this.outputs = outputs;
    }

    /**
     * gets the placement info
     * @return pl info
     */
    public PlacementInfo getPlacementInfo() {
        return placementInfo;
    }

    /**
     * sets the placement info
     * @param placementInfo pl info
     */
    public void setPlacementInfo(PlacementInfo placementInfo) {
        this.placementInfo = placementInfo;
    }

    @Override
    public String toString() {
        return "Block (" + name + "): " + "I: " + inputs.size() + ", O: " + outputs.size() + ", PlInfo: " + placementInfo;
    }

    /**
     * returns if the block is gated (e.g. is it configured as a FF)
     * @return
     */
    public boolean isGated() {
        return gated;
    }

    /**
     * sets if the block is gated
     * @param gated
     */
    public void setGated(boolean gated) {
        this.gated = gated;
    }
}
