package com.ece6133.model.timing;

public class NetNode {
    private String name = "";
    private Block parent = null;

    public NetNode(final String name) {
        //TODO canonicalize
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }
}
