package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.arch.k6_n10.K6Model;

import java.util.ArrayList;

public class Subckt {
    private String name;
    private String typeName;
    private K6Model backingType = null;
    private ArrayList<PortAssn> inputs = new ArrayList<>();
    private ArrayList<NetNode> inputDrivingNet = new ArrayList<>();
    private ArrayList<PortAssn> outputs = new ArrayList<>();
    private ArrayList<NetNode> drivenOutputNets = new ArrayList<>();

    public Subckt() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ArrayList<PortAssn> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<PortAssn> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<NetNode> getInputDrivingNet() {
        return inputDrivingNet;
    }

    public void setInputDrivingNet(ArrayList<NetNode> inputDrivingNet) {
        this.inputDrivingNet = inputDrivingNet;
    }

    public ArrayList<PortAssn> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<PortAssn> outputs) {
        this.outputs = outputs;
    }

    public ArrayList<NetNode> getDrivenOutputNets() {
        return drivenOutputNets;
    }

    public void setDrivenOutputNets(ArrayList<NetNode> drivenOutputNets) {
        this.drivenOutputNets = drivenOutputNets;
    }

    public K6Model getBackingType() {
        return backingType;
    }

    public void setBackingType(K6Model backingType) {
        this.backingType = backingType;
    }
}
