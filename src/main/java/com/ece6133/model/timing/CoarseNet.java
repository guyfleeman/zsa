package com.ece6133.model.timing;

import java.util.ArrayList;

public class CoarseNet {
    public String name = "";
    public NetNode source = null;
    public ArrayList<NetNode> sinks = null;
    public int longestRectDist = -1;
    public float longestDelay = -1.0f;
}
