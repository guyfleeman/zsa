package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.timing.NetNode;

/**
 * tuple associating a port with a net
 */
public class PortAssn {
    private Port assignment;
    private NetNode driver;

    /**
     *
     * @param assignment
     * @param driver
     */
    public PortAssn(Port assignment, NetNode driver) {
        this.setAssignment(assignment);
        this.setDriver(driver);
    }

    /**
     *
     * @return
     */
    public Port getAssignment() {
        return assignment;
    }

    /**
     *
     * @param assignment
     */
    public void setAssignment(Port assignment) {
        this.assignment = assignment;
    }

    /**
     *
     * @return
     */
    public NetNode getDriver() {
        return driver;
    }

    /**
     *
     * @param driver
     */
    public void setDriver(NetNode driver) {
        this.driver = driver;
    }
}
