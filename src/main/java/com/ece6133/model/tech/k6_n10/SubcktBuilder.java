package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.arch.k6_n10.K6Arch;
import com.ece6133.model.timing.NetNode;

public class SubcktBuilder {
    protected static SubcktBuilder instance;

    static {
        instance = new SubcktBuilder();
    }

    public static SubcktBuilder getInstance() {
        return instance;
    }

    private boolean buildActive = false;
    private Subckt subckt = null;
    private K6Arch arch;

    private SubcktBuilder() {}

    public void resetBuild() {
        buildActive = false;
    }

    public void flagStartBuild(final K6Arch arch) {
        this.arch = arch;
        subckt = new Subckt();
        buildActive = true;
    }

    public boolean isBuildActive() {
        return buildActive;
    }

    public void appendDefLine(String line) {
        int startIndex = 0;
        String[] els = line.split(" ");
        if (line.contains(".subckt")) {
            if (!els[0].equalsIgnoreCase(".subckt")) {
                throw new RuntimeException("invalid subckt header");
            }

            subckt.setTypeName(els[1]);
            if (!arch.supportsSubckt(subckt.getName())) {
                throw new RuntimeException("cannot build subckt for unsupported subckt type");
            }

            subckt.setBackingType(arch.getModelByName(subckt.getTypeName()));
            startIndex = 2;
        }

        for (int i = startIndex; i < els.length; i++) {
            if (els[1].equalsIgnoreCase("\\")) {
                break;
            }

            String[] portBind = els[i].split("=");
            String assignment = portBind[0];
            String driver = portBind[1];

            String port = assignment;
            short index = -1;
            if (assignment.contains("]")) {
                String[] portComp = assignment.replaceAll("]", "").split("\\[");
                port = portComp[0];
                index = Short.parseShort(portComp[1]);
            }

            Port assn = new Port(port, index);
            NetNode driverNode = new NetNode(driver);
            PortAssn subcktPort = new PortAssn(assn, driverNode);
            if (subckt.getBackingType().getInputPortNames().contains(assignment)) {
                subckt.getInputs().add(subcktPort);
                subckt.getInputDrivingNet().add(driverNode);
            } else {
                subckt.getOutputs().add(subcktPort);
                subckt.getDrivenOutputNets().add(driverNode);
            }
        }
    }

    public Subckt get() {
        return subckt;
    }
}
