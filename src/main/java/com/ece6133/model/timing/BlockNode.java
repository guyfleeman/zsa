package com.ece6133.model.timing;

import java.util.ArrayList;

/**
 * A wrapper for {@link Block} that contains references to upstream and downstream Blocks and downstream paths
 * Useful for constructing a DAG for the paths
 */
public class BlockNode {
    private Block block;
    private ArrayList<BlockNode> immediateUpstreamBlocks = new ArrayList<>();
    private ArrayList<BlockNode> immediateDownstreamBlocks = new ArrayList<>();
    private ArrayList<CoarsePath> downstreamPaths = new ArrayList<>();

    /**
     * creates a blocknode from a block
     * @param b contained block
     */
    public BlockNode(Block b) {
        setBlock(b);
    }

    /**
     * gets the backing block
     * @return backing block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * sets the backing block
     * @param block backing block
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     *
     * @return
     */
    public ArrayList<BlockNode> getImmediateUpstreamBlocks() {
        return immediateUpstreamBlocks;
    }

    /**
     *
     * @param immediateUpstreamBlocks
     */
    public void setImmediateUpstreamBlocks(ArrayList<BlockNode> immediateUpstreamBlocks) {
        this.immediateUpstreamBlocks = immediateUpstreamBlocks;
    }

    /**
     *
     * @return
     */
    public ArrayList<BlockNode> getImmediateDownstreamBlocks() {
        return immediateDownstreamBlocks;
    }

    /**
     *
     * @param immediateDownstreamBlocks
     */
    public void setImmediateDownstreamBlocks(ArrayList<BlockNode> immediateDownstreamBlocks) {
        this.immediateDownstreamBlocks = immediateDownstreamBlocks;
    }

    /**
     *
     * @return
     */
    public ArrayList<CoarsePath> getDownstreamPaths() {
        return downstreamPaths;
    }

    /**
     *
     * @param downstreamPaths
     */
    public void setDownstreamPaths(ArrayList<CoarsePath> downstreamPaths) {
        this.downstreamPaths = downstreamPaths;
    }
}
