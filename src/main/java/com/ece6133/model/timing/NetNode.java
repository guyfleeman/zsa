package com.ece6133.model.timing;

/**
 * a net node is a wrapper around the name String to provide the ability to canonicalize names
 */
public class NetNode {
    private String name = "";
    private Block parent = null;

    /**
     *
     * @param name name
     */
    public NetNode(final String name) {
        //TODO canonicalize
        this.setName(name);
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
     * gets the parent of the net node/port
     * @return
     */
    public Block getParent() {
        return parent;
    }

    /**
     * sets the parent of the net node/port
     * @param parent
     */
    public void setParent(Block parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "NetNode: N: " + name + ", P: " + parent;
    }

}
