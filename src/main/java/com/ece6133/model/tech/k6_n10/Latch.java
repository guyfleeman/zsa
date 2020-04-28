package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

/**
 * represents a latch and its configuration
 */
public class Latch {
    /**
     *
     * @return
     */
    public NetNode getInput() {
        return input;
    }

    /**
     *
     * @param input
     */
    public void setInput(NetNode input) {
        this.input = input;
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

    /**
     * gets the latch trigger type
     * @return
     */
    public LatchType getLatchType() {
        return latchType;
    }

    /**
     * sets the latch trigger type
     * @param latchType
     */
    public void setLatchType(LatchType latchType) {
        this.latchType = latchType;
    }

    /**
     * gets the name of the clock network clocking the latch
     * @return
     */
    public NetNode getClk() {
        return clk;
    }

    /**
     * sets the name of the clk network clocking the latch
     * @param clk
     */
    public void setClk(NetNode clk) {
        this.clk = clk;
    }

    /**
     * gets the initial value (includes none, DC, UNK)
     * @return
     */
    public String getInitialValue() {
        return initialValue;
    }

    /**
     *
     */
    public enum LatchType {
        FE_FALLING_EDGE,
        RE_RISING_EDGE,
        AH_ACTIVE_HIGH,
        AL_ACTIVE_LOW,
        AS_ASYNC
    }

    private NetNode input = null;
    private NetNode output = null;
    private LatchType latchType = null;
    private NetNode clk = null;
    protected String initialValue = null;

    public Latch() {}

    /**
     * creates a latch
     * @param input input net
     * @param output output net
     * @param latchType trigger type
     * @param clk driving clock
     * @param initialValue initial value
     */
    public Latch(final NetNode input,
                 final NetNode output,
                 final LatchType latchType,
                 final NetNode clk,
                 final String initialValue) {
        this.setInput(input);
        this.setOutput(output);
        this.setLatchType(latchType);
        this.setClk(clk);
        this.initialValue = initialValue;
    }

    /**
     * converts the schema notation to internal latch type
     * @param lt notation string from schema
     * @return latchType
     */
    public static LatchType strToLatchType(final String lt) {
        switch (lt.toLowerCase()) {
            case "fe": return LatchType.FE_FALLING_EDGE;
            case "re": return LatchType.RE_RISING_EDGE;
            case "ah": return LatchType.AH_ACTIVE_HIGH;
            case "al": return LatchType.AL_ACTIVE_LOW;
            case "as": return LatchType.AS_ASYNC;
            default: throw new RuntimeException("unknown latch code");
        }
    }
}
