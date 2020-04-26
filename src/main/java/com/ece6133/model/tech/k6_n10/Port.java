package com.ece6133.model.tech.k6_n10;

public class Port {
    private String name = "";
    private short index = 0;

    public Port() {}

    public Port(String name, short index) {
        this.setName(name);
        this.setIndex(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getIndex() {
        return index;
    }

    public void setIndex(short index) {
        this.index = index;
    }
}
