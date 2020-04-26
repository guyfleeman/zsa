package com.ece6133.model.arch.k6_n10;

import java.util.ArrayList;

public class K6Model {
    private String name;
    private ArrayList<String> inputPortNames = new ArrayList<>();
    private ArrayList<String> outputPortNames = new ArrayList<>();

    public K6Model() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addInputPortName(final String name) {
        this.inputPortNames.add(name);
    }

    public ArrayList<String> getInputPortNames() {
        return inputPortNames;
    }

    public void setInputPortNames(ArrayList<String> inputPortNames) {
        this.inputPortNames = inputPortNames;
    }

    public void addOutputPortName(final String name) {
        this.outputPortNames.add(name);
    }

    public ArrayList<String> getOutputPortNames() {
        return outputPortNames;
    }

    public void setOutputPortNames(ArrayList<String> outputPortNames) {
        this.outputPortNames = outputPortNames;
    }
}
