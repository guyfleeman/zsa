package com.ece6133.model.tech.k6_n10;

public class Latch {
    public NetNode getInput() {
        return input;
    }

    public void setInput(NetNode input) {
        this.input = input;
    }

    public NetNode getOutput() {
        return output;
    }

    public void setOutput(NetNode output) {
        this.output = output;
    }

    public LatchType getLatchType() {
        return latchType;
    }

    public void setLatchType(LatchType latchType) {
        this.latchType = latchType;
    }

    public NetNode getClk() {
        return clk;
    }

    public void setClk(NetNode clk) {
        this.clk = clk;
    }

    public String getInitialValue() {
        return initialValue;
    }

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
