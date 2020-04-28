package com.ece6133.model.tech.k6_n10;

/**
 * tuple for name index pair
 */
public class Port {
    private String name = "";
    private short index = 0;

    public Port() {}

    /**
     *
     * @param name
     * @param index
     */
    public Port(String name, short index) {
        this.setName(name);
        this.setIndex(index);
    }

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
     *
     * @return
     */
    public short getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(short index) {
        this.index = index;
    }
}
