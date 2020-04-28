package com.ece6133.model.timing;

import com.ece6133.model.arch.Arch;
import com.ece6133.model.arch.k6_n10.K6Arch;
import com.ece6133.model.tech.k6_n10.Latch;
import com.ece6133.model.tech.k6_n10.Lut;
import com.ece6133.model.tech.k6_n10.Subckt;
import com.ece6133.model.timing.NetNode;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class K6DesignModel {
    private Arch arch = new K6Arch();
    private String name = "";
    private ArrayList<NetNode> inputs = new ArrayList<>();
    private ArrayList<NetNode> outputs = new ArrayList<>();
    private ArrayList<Latch> latches = new ArrayList<>();
    private ArrayList<Lut> luts = new ArrayList<>();
    private ArrayList<Subckt> subckts = new ArrayList<>();

    public K6DesignModel() {

    }

    public ArrayList<NetNode> getInputs() {
        return inputs;
    }

    public void addInput(@NotNull final NetNode newInput) {
        this.inputs.add(newInput);
    }

    public ArrayList<NetNode> getOutputs() {
        return outputs;
    }

    public void addOutput(@NotNull final NetNode newOutput) {
        this.outputs.add(newOutput);
    }

    public ArrayList<Latch> getLatches() {
        return latches;
    }

    public void addLatch(@NotNull final Latch newLatch) {
        this.latches.add(newLatch);
    }

    public ArrayList<Lut> getLuts() {
        return luts;
    }

    public void addLut(@NotNull final Lut newLut) {
        this.luts.add(newLut);
    }

    public ArrayList<Subckt> getSubckts() {
        return subckts;
    }

    public void addSubckt(@NotNull final Subckt newSubckt) {
        this.subckts.add(newSubckt);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
