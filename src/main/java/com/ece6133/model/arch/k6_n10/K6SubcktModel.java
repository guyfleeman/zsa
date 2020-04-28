package com.ece6133.model.arch.k6_n10;

import java.util.ArrayList;

/**
 * model of a K6 Subckt (hard tile macros)
 *
 * could be LUT, memory tile, mult, PLL etc
 */
public class K6SubcktModel {
    private String name;
    private ArrayList<String> inputPortNames = new ArrayList<>();
    private ArrayList<String> outputPortNames = new ArrayList<>();

    public K6SubcktModel() {}


    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * adds an input port name
     * @param name port name
     */
    public void addInputPortName(final String name) {
        this.inputPortNames.add(name);
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getInputPortNames() {
        return inputPortNames;
    }

    /**
     *
     * @param inputPortNames
     */
    public void setInputPortNames(ArrayList<String> inputPortNames) {
        this.inputPortNames = inputPortNames;
    }

    /**
     * adds an output port name
     * @param name
     */
    public void addOutputPortName(final String name) {
        this.outputPortNames.add(name);
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getOutputPortNames() {
        return outputPortNames;
    }

    /**
     *
     * @param outputPortNames
     */
    public void setOutputPortNames(ArrayList<String> outputPortNames) {
        this.outputPortNames = outputPortNames;
    }
}
