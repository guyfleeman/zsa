package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

public class PortAssn {
    private Port assignment;
    private NetNode driver;

    public PortAssn(Port assignment, NetNode driver) {
        this.setAssignment(assignment);
        this.setDriver(driver);
    }

    public Port getAssignment() {
        return assignment;
    }

    public void setAssignment(Port assignment) {
        this.assignment = assignment;
    }

    public NetNode getDriver() {
        return driver;
    }

    public void setDriver(NetNode driver) {
        this.driver = driver;
    }
}
