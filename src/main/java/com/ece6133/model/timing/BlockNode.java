package com.ece6133.model.timing;

import java.util.ArrayList;

public class BlockNode {
    private Block block;
    private ArrayList<BlockNode> immediateUpstreamBlocks = new ArrayList<>();
    private ArrayList<BlockNode> immediateDownstreamBlocks = new ArrayList<>();
    private ArrayList<CoarsePath> downstreamPaths = new ArrayList<>();

    public BlockNode(Block b) {
        setBlock(b);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public ArrayList<BlockNode> getImmediateUpstreamBlocks() {
        return immediateUpstreamBlocks;
    }

    public void setImmediateUpstreamBlocks(ArrayList<BlockNode> immediateUpstreamBlocks) {
        this.immediateUpstreamBlocks = immediateUpstreamBlocks;
    }

    public ArrayList<BlockNode> getImmediateDownstreamBlocks() {
        return immediateDownstreamBlocks;
    }

    public void setImmediateDownstreamBlocks(ArrayList<BlockNode> immediateDownstreamBlocks) {
        this.immediateDownstreamBlocks = immediateDownstreamBlocks;
    }

    public ArrayList<CoarsePath> getDownstreamPaths() {
        return downstreamPaths;
    }

    public void setDownstreamPaths(ArrayList<CoarsePath> downstreamPaths) {
        this.downstreamPaths = downstreamPaths;
    }
}
