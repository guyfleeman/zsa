package com.ece6133.model.timing;

import com.ece6133.model.arch.Arch;
import com.ece6133.model.arch.k6_n10.K6Arch;
import com.ece6133.model.tech.k6_n10.Latch;
import com.ece6133.model.tech.k6_n10.Lut;
import com.ece6133.model.tech.k6_n10.Subckt;
import com.ece6133.model.timing.NetNode;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * represents a design for the K6 arch.
 */
public class K6DesignModel {
    /**
     * backing arch definition
     */
    private Arch arch = new K6Arch();

    /**
     * design name
     */
    private String name = "";

    /**
     * all input wires
     */
    private ArrayList<NetNode> inputs = new ArrayList<>();

    /**
     * all output wires
     */
    private ArrayList<NetNode> outputs = new ArrayList<>();

    /**
     * all instantiated latches and their configuration
     */
    private ArrayList<Latch> latches = new ArrayList<>();

    /**
     * all instantiated LUTs and their configuration
     */
    private ArrayList<Lut> luts = new ArrayList<>();

    /**
     * all instantiated subcks and their configuration
     *
     * these correspond to hard tile macros
     */
    private ArrayList<Subckt> subckts = new ArrayList<>();

    /**
     * all placement info for the design
     */
    private HashMap<String, PlacementInfo> plInfo;

    /**
     * all blocks for the design
     */
    private HashMap<String, Block> blocks;

    public K6DesignModel() {

    }

    /**
     * get number of blocks
     * @return
     */
    public int getNumBlocks() {
        return blocks.size();
    }

    /**
     * get slack of block
     * @param name the string name of the block
     * @return 
     */
    public int getSlack(String name) {
        return blocks.get(name).getSlack();
    }

    /**
     * get array of block names
     * @return String[] of names
     */
    public String[] getBlockNames() {
        return blocks.keySet().toArray();
    }

    /**
     * get all input wires
     * @return input wires
     */
    public ArrayList<NetNode> getInputs() {
        return inputs;
    }

    /**
     * add delay to block equivalent to block's slack
     * @param name the string name of the block
     * @return 
     */
    public void addSlackDelay(String name) {
        int slack = getSlack(name);
        blocks.get(name).addDelay(slack);
    }

    /**
     * add an input to the list of input wires
     * @param newInput input wire
     */
    public void addInput(@NotNull final NetNode newInput) {
        this.inputs.add(newInput);
    }

    /**
     * get all output wires
     * @return output wries
     */
    public ArrayList<NetNode> getOutputs() {
        return outputs;
    }

    /**
     * add an output to the list of output wires
     * @param newOutput output wire
     */
    public void addOutput(@NotNull final NetNode newOutput) {
        this.outputs.add(newOutput);
    }

    /**
     * get all latches
     * @return latches
     */
    public ArrayList<Latch> getLatches() {
        return latches;
    }

    /**
     * add a latch to the design
     * @param newLatch latch
     */
    public void addLatch(@NotNull final Latch newLatch) {
        this.latches.add(newLatch);
    }

    /**
     * get all LUTs
     * @return luts
     */
    public ArrayList<Lut> getLuts() {
        return luts;
    }

    /**
     * add a lut to the list of LUTS
     * @param newLut lut
     */
    public void addLut(@NotNull final Lut newLut) {
        this.luts.add(newLut);
    }

    /**
     * get all subckts (hard tile macros) for the design
     * @return subckts
     */
    public ArrayList<Subckt> getSubckts() {
        return subckts;
    }

    /**
     * add a subckt to the list of subckts
     * @param newSubckt subckt
     */
    public void addSubckt(@NotNull final Subckt newSubckt) {
        this.subckts.add(newSubckt);
    }

    /**
     * get the design name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set the design name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get all placement info
     * @return pl info
     */
    public HashMap<String, PlacementInfo> getPlInfo() {
        return plInfo;
    }

    /**
     * set all pl info
     * @param plInfo pl info
     */
    public void setPlInfo(HashMap<String, PlacementInfo> plInfo) {
        this.plInfo = plInfo;
    }

    /**
     * get all blocks
     * @return blocks
     */
    public HashMap<String, Block> getBlocks() {
        return blocks;
    }

    /**
     * set blocks
     * @param blocks blocks
     */
    public void setBlocks(HashMap<String, Block> blocks) {
        this.blocks = blocks;
    }
}
