package com.ece6133.model.timing;

import com.ece6133.model.arch.k6_n10.K6Arch;
import com.ece6133.model.tech.k6_n10.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is responsible for build a model of the K6 architecture used by VPR to
 * test FPGA/EDA tooling development.
 * <p>
 * The model is defined by several artifacts generated by the early stages of the tool flow.
 * <p>
 * *.pre-vpr.blif defines the post-opt RTL including architecture agnostic (dimensionally unbounded)
 * primitives. In this file multipliers, memories, and LUTs have no bounds.
 * *.abc.blif is the post-tech mapped RTL. This defines LUTs and Mult subckt instantiations that
 * are properly bound to the architecture
 * *.net defines the netlists, mostly. There are some hidden nets that seeming get optimized away between
 * tech map and placement. Notable FF to FF nets and LUT[n] to FF nets with a single CLB disappear as a internal
 * direct connection in the crossbar.
 * *.place defines the grid placement (in squares convertible to microns) of instances
 * *.route is currently unused, but it defines the specific tracks that routes take
 */
public class K6DesignModelLoader {
    /**
     * private ctr, loaders are static
     */
    private K6DesignModelLoader() {
    }

    /**
     * Used to track line ctx mode for blif parsing
     * <p>
     * BLIF isn't LL(n) due to unbounded LUT ON-SET length, so the parser must
     * must be push down compatible (though this is hacked to N=1 because LUTs are flattened in the LIF)
     */
    private enum ParseCtxMode {
        MODEL,
        INPUT,
        OUTPUT,
        LATCH,
        SUBCKT,
        LUT,
        NONE
    }

    /**
     * loads the core placement element definitions from a tech mapped blif file
     * <b>NOTE: hard mem is currently unsupported</b>
     *
     * @param modelBlif a tech mapped blif file
     * @param arch      a an architecture definition XML file, contains subckt and timing defs
     * @return a K6DesignModel populated with instance data
     * @throws IOException variety of reasons related to file IO. Check perms and file types
     */
    public static K6DesignModel loadModel(final File modelBlif, final K6Arch arch) throws IOException {
        final BufferedReader fileReader = new BufferedReader(new FileReader(modelBlif));

        K6DesignModel model = new K6DesignModel();

        ParseCtxMode activeCtx = ParseCtxMode.NONE;
        String curLine;
        while ((curLine = fileReader.readLine()) != null) {
            if (curLine.equals("")) {
                /*
                 * when leaving a build context we need to finalize any advanced construction
                 */
                activeCtx = ParseCtxMode.NONE;
                if (SubcktBuilder.getInstance().isBuildActive()) {
                    Subckt newSubckt = SubcktBuilder.getInstance().get();
                    model.addSubckt(newSubckt);
                }

                if (LutBuilder.getInstance().isBuildActive()) {
                    Lut newLut = LutBuilder.getInstance().get();
                    model.addLut(newLut);
                }

                continue;
            } else if (curLine.charAt(0) == '#') {
                continue;
            } else if (curLine.startsWith(".model")) {
                activeCtx = ParseCtxMode.MODEL;
            } else if (curLine.startsWith(".inputs")) {
                activeCtx = ParseCtxMode.INPUT;
            } else if (curLine.startsWith(".outputs")) {
                activeCtx = ParseCtxMode.OUTPUT;
            } else if (curLine.startsWith(".latch")) {
                activeCtx = ParseCtxMode.LATCH;
            } else if (curLine.startsWith(".subckt")) {
                SubcktBuilder.getInstance().resetBuild();
                SubcktBuilder.getInstance().flagStartBuild(arch);
                activeCtx = ParseCtxMode.SUBCKT;
            } else if (curLine.startsWith(".names")) {
                LutBuilder.getInstance().resetBuild();
                LutBuilder.getInstance().flagStartBuild();
                activeCtx = ParseCtxMode.LUT;
            }

            buildModelWithContext(activeCtx, model, curLine);
        }

        return null;
    }

    /**
     * helper function to append line data to the model based on the identified ctx
     *
     * @param curCtx  the current context determined by the parser
     * @param model   the model
     * @param curLine the line being parsed
     */
    private static void buildModelWithContext(final ParseCtxMode curCtx, K6DesignModel model, final String curLine) {
        // dispatch a specific context parser
        switch (curCtx) {
            case MODEL:
                break;
            case INPUT:
                buildModelWithInputCtx(model, curLine);
                break;
            case OUTPUT:
                buildModelWithOutputCtx(model, curLine);
                break;
            case LATCH:
                buildModelWithLatchCtx(model, curLine);
                break;
            case SUBCKT:
                buildModelWithSubcktCtx(curLine);
                break;
            case LUT:
                buildModelWithLutCtx(curLine);
                break;
            default:
                throw new RuntimeException("unreachable");
        }
    }

    /**
     * helper function to load driven input net names
     *
     * @param model   the model
     * @param curLine the line being parsed
     */
    private static void buildModelWithInputCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
        for (String el : elements) {
            if (el.equalsIgnoreCase(".inputs")) {
                continue;
            } else if (el.equalsIgnoreCase("\\")) {
                return;
            } else {
                model.addInput(new NetNode(el));
            }
        }
    }

    /**
     * helper function to load driving net names
     *
     * @param model   the model
     * @param curLine the line being parsed
     */
    private static void buildModelWithOutputCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
        for (String el : elements) {
            if (el.equalsIgnoreCase(".outputs")) {
                continue;
            } else if (el.equalsIgnoreCase("\\")) {
                return;
            } else {
                model.addInput(new NetNode(el));
            }
        }
    }

    /**
     * helper function to load latch instance
     *
     * @param model   the model
     * @param curLine the current line
     */
    private static void buildModelWithLatchCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
        // latches shouldn't be more than one line
        // throw an error if a LCC is present
        if (Arrays.asList(elements).contains("\\")) {
            throw new RuntimeException("unsupported delegated context for latch in blif");
        }

        if (!(elements.length == 5 || elements.length == 6)) {
            throw new RuntimeException("unsupported schema for latch in blif");
        }

        final String inputNet = elements[1];
        final String outputNet = elements[2];
        final String latchType = elements[3];
        final String clkNet = elements[4];
        final String initVal = (elements.length == 6) ? elements[5] : null;

        final Latch newLatch = new Latch(new NetNode(inputNet),
                new NetNode(outputNet),
                Latch.strToLatchType(latchType),
                new NetNode(clkNet),
                initVal);

        model.addLatch(newLatch);
    }

    /**
     * helper function to load subckts
     * <p>
     * these are more complex and delegated to a builder
     *
     * @param curLine the current line
     */
    private static void buildModelWithSubcktCtx(final String curLine) {
        SubcktBuilder.getInstance().appendDefLine(curLine);
    }

    /**
     * helper function to load LUTs
     * <p>
     * these are more complex and delegated to a builder
     *
     * @param curLine the current line
     */
    private static void buildModelWithLutCtx(final String curLine) {
        LutBuilder.getInstance().appendDefLine(curLine);
    }

    /**
     * @param netBlocks block BLIF file location
     * @param placeFile placement file
     * @param model active model
     * @throws IOException file errors
     * @throws XPathExpressionException malformed xml tree
     * @throws SAXException malformed xml syntax
     * @throws ParserConfigurationException unreachable (probably :P)
     */
    public static void loadPlacement(final File netBlocks, final File placeFile, K6DesignModel model)
            throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = parsePlacementFile(placeFile);
        HashMap<String, Block> blocks = loadNetBlocks(netBlocks);
        mapPlacements(blocks, plInfo);
    }

    /**
     * build a map of net bound tile names to their placement info
     * @param placeFile placement file
     * @return populated placement map
     * @throws IOException file errors
     */
    public static HashMap<String, PlacementInfo> parsePlacementFile(final File placeFile) throws IOException {
        final BufferedReader fileReader = new BufferedReader(new FileReader(placeFile));
        HashMap<String, PlacementInfo> plInfo = new HashMap<>();
        String curLine;
        while ((curLine = fileReader.readLine()) != null) {
            if (curLine.equalsIgnoreCase("")
                    || curLine.startsWith("#")
                    || curLine.startsWith("Netlist_File")
                    || curLine.startsWith("Array")) {
                continue;
            }

            String[] dirtyEls = curLine.split("\t");
            ArrayList<String> els = new ArrayList<>();
            for (String el : dirtyEls) {
                if (!el.equalsIgnoreCase("")) {
                    els.add(el);
                }
            }
            if (els.size() != 5) {
                throw new RuntimeException("pl file parse error");
            }

            PlacementInfo newPlacement = new PlacementInfo();
            newPlacement.name = els.get(0);
            newPlacement.x = Integer.parseInt(els.get(1));
            newPlacement.y = Integer.parseInt(els.get(2));
            newPlacement.subblk = Integer.parseInt(els.get(3));
            newPlacement._blknum = Integer.parseInt(els.get(4).replaceAll("#", ""));

            plInfo.put(newPlacement.name, newPlacement);
        }

        return plInfo;
    }

    /**
     * assign placement references to blocks, canonicalize key names, and build a key/Block map
     * @param blocks blocks
     * @param plInfo placements
     */
    public static void mapPlacements(HashMap<String, Block> blocks, HashMap<String, PlacementInfo> plInfo) {
        for (String k : blocks.keySet()) {
            if (!plInfo.containsKey(k)) {
                throw new RuntimeException("key not found: " + k);
            }

            blocks.get(k).setPlacementInfo(plInfo.get(k));
        }
    }

    /**
     * loads all top level block and populates relevant subblock data
     *
     * A full subblock heirarchy isn't required for nets, paths, and delays but some metadata is
     * relevant. Especially if CLBs have the FF open or bound, and if bound if that net is BLE/CLB
     * local, rte local, or anything else.
     * @param netFile netlist xml file
     * @return a map of block names to blocks
     * @throws ParserConfigurationException malformed xml
     * @throws IOException file io errors (perms, location, etc)
     * @throws SAXException malformed xml
     */
    public static HashMap<String, Block> loadNetBlocks(final File netFile)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document archDef = builder.parse(netFile);
        archDef.getDocumentElement().normalize();

        Element root = archDef.getDocumentElement();

        HashMap<String, Block> blocks = new HashMap<>();
        NodeList topLevelChildBlocks = root.getChildNodes();
        for (int i = 0; i < topLevelChildBlocks.getLength(); i++) {
            Node topLevelChild = topLevelChildBlocks.item(i);
            if (!(topLevelChild instanceof Element) || !topLevelChild.getNodeName().equalsIgnoreCase("block") || topLevelChild.getParentNode() != root) {
                continue;
            }

            Element topLevelChildE = (Element) topLevelChild;

            Block tlBlock = new Block();
            tlBlock.setName(topLevelChildE.getAttribute("name"));

            Element inputsEl = (Element) topLevelChildE.getElementsByTagName("inputs").item(0);
            addInputs(inputsEl, tlBlock);

            // outputs are special children because there's an implicit mapping from subblock configurations and their
            // port names to the actual outputs listed for the top level block which are all internal routes or
            // crossbar routes. So we ignore the listed outputs and descend. See function JD for more info
            //Element outputsEl = (Element) topLevelChildE.getElementsByTagName("outputs").item(0);
            addOutputs(topLevelChildE, tlBlock);

            Element clocksEl = (Element) topLevelChildE.getElementsByTagName("clocks").item(0);
            assignGated(clocksEl, tlBlock);

            blocks.put(tlBlock.getName(), tlBlock);
        }

        return blocks;
    }

    /**
     * parse top level inputs
     * @param inputsEl inputs element/tag
     * @param blk active block
     */
    public static void addInputs(Element inputsEl, Block blk) {
        NodeList ports = inputsEl.getElementsByTagName("port");
        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            ArrayList<String> netNames = new ArrayList<>(Arrays.asList(port.getTextContent().split(" ")));
            blk.getInputs().put(portName, netNames);
        }
    }

    /**
     * parse outputs
     *
     * top level outputs are always to a local route or local crossbar route, none of which are easily accessible
     * at a high level. In reality these crossbar nets are mapped through an architecture map from the CLB/BLE global
     * port index, through a crossbar/ble index, to the subblock index (ewwww). Thankfully, the lowest children contain
     * a ref to the output non-local net name. This is under the port tag with name "out" not "O". Additionally FFs,
     * if used, contain a non-local net ref under the port tag with name "Q".
     * @param outputsEl the highest parent in the local block heir.
     * @param blk active block
     */
    public static void addOutputs(Element outputsEl, Block blk) {
        // man this little fucker was hidden....
        // the FF have an implicit internal crossbar to the BLE output
        // iff the BLE is a lut5/6 and intermediate net from the BLIF
        // isn't a block, sub-block, or sub-sub-block of the top the level block
        // block name referenced by either global endpoint of the BLE or FF
        // NOTE: some of these *still* don't recover correctly >.<
        boolean flagFFDirectCrossbarEnabled = false;

        NodeList ports = outputsEl.getElementsByTagName("port");
        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            if (portName.equalsIgnoreCase("Q")) {
                ArrayList<String> netNames = new ArrayList<>(Arrays.asList(port.getTextContent().split(" ")));
                blk.getOutputs().put(portName, netNames);
            }

            if (portName.equalsIgnoreCase("D")) {
                String portText = port.getTextContent();
                if (portText.contains(".out[0]") && portText.contains("direct")) {
                    flagFFDirectCrossbarEnabled = true;
                }
            }
        }

        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            if (portName.equalsIgnoreCase("out")) {
                if (!flagFFDirectCrossbarEnabled) {
                    ArrayList<String> netNames = new ArrayList<>(Arrays.asList(port.getTextContent().split(" ")));
                    blk.getOutputs().put(portName, netNames);
                }
            }
        }
    }

    /**
     * checks if the global clk is bound to the block
     *
     * this hints that an FF is inuse, and if it isn't we have async logic
     * which is unsupported by ZSA/ML
     * @param clkEl clk xml element of the tl block
     * @param blk the tl block
     */
    public static void assignGated(Element clkEl, Block blk) {
        NodeList ports = clkEl.getElementsByTagName("port");
        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            if (portName.equals("clk")) {
                if (!port.getTextContent().equalsIgnoreCase("open")) {
                    blk.setGated(true);
                }
            }
        }
    }

    /**
     * analysis function to bypass broken nets due to the invisible internal nets
     * @param blocks the block db
     * @return the netlist
     * @deprecated
     */
    public static CoarseNetlist buildCoarseNetlistBypassIC(final HashMap<String, Block> blocks) {
        CoarseNetlist cnl = new CoarseNetlist();

        for (String k : blocks.keySet()) {
            Block b = blocks.get(k);
            buildCoarseNetsForIRLBlock(b, blocks, cnl);
        }

        return cnl;
    }

    /**
     * helper function to the broken net bypass analysis func
     * @param b block
     * @param blocks block db
     * @param netlist current netlist
     * @deprecated
     */
    public static void buildCoarseNetsForIRLBlock(Block b, final HashMap<String, Block> blocks, CoarseNetlist netlist) {
        CoarseNet net = new CoarseNet();
        net.source = new NetNode(b.getName());
        net.source.setParent(b);
        bindNetSinks(net, blocks);
        netlist.addCoarseNet(net);
    }

    /**
     * builds the net list for the design
     * @param blocks block db
     * @return the netlist
     */
    public static CoarseNetlist buildCoarseNetlist(final HashMap<String, Block> blocks) {
        CoarseNetlist cnl = new CoarseNetlist();

        for (String k : blocks.keySet()) {
            Block b = blocks.get(k);
            buildCoarseNetsForBlock(b, blocks, cnl);
        }

        return cnl;
    }

    /**
     * builds the netlist associated with a single block and its downstream nodes (fanout cone based)
     * @param b current block
     * @param blocks block db
     * @param netlist current global netlist
     */
    public static void buildCoarseNetsForBlock(Block b, final HashMap<String, Block> blocks, CoarseNetlist netlist) {
        for (String oPortName : b.getOutputs().keySet()) {
            ArrayList<String> drivenPortNets = b.getOutputs().get(oPortName);
            for (String drivenNetName : drivenPortNets) {
                if (drivenNetName.equalsIgnoreCase("open") || drivenNetName.contains("fle")) {
                    continue;
                }

                CoarseNet net = new CoarseNet();
                net.source = new NetNode(drivenNetName);
                net.source.setParent(b);
                bindNetSinks(net, blocks);
                netlist.addCoarseNet(net);
            }
        }
    }

    /**
     * generate parentage references for a global net
     *
     * this assists path construction later
     * @param net a single global net
     * @param blocks block db
     */
    public static void bindNetSinks(CoarseNet net, final HashMap<String, Block> blocks) {
        for (Block b : blocks.values()) {
            for (String inputPortKey : b.getInputs().keySet()) {
                ArrayList<String> inputPortNetNames = b.getInputs().get(inputPortKey);
                for (String inputPortNetName : inputPortNetNames) {
                    if (inputPortNetName.equals(net.source.getName())) {
                        NetNode sink = new NetNode(inputPortNetName);
                        sink.setParent(b);
                        net.sinks.add(sink);
                    }
                }
            }
        }
    }

    /**
     * recover timed paths from the global netlist and block/tile db. AKA the big daddy
     * @param netlist the global netlist
     * @param blocks the block/tile db
     * @return path list
     */
    public static CoarsePathList recoverPaths(CoarseNetlist netlist, final HashMap<String, Block> blocks) {
        ArrayList<CoarsePathSegment> coarsePathSegments = buildCoarsePathSegements(netlist);
        HashMap<Block, BlockNode> timingGraph = buildPathGraphNodes(blocks, coarsePathSegments);
        ArrayList<CoarsePath> gatedCoarsePaths = buildCoarsePathsFromGraph(timingGraph, coarsePathSegments);

        CoarsePathList cpl = new CoarsePathList();
        cpl.setTimingGraphNodes(timingGraph);
        cpl.getCoarsePathSegments().addAll(coarsePathSegments);
        cpl.getCoarsePaths().addAll(gatedCoarsePaths);

        return cpl;
    }

    /**
     * wrap all blocks as graph nodes, and calculate all upsteam and downstream graph nodes based on paths
     * @param blocks block db
     * @param coarsePathSegments all path segments
     * @return mapped path graph nodes
     */
    public static HashMap<Block, BlockNode> buildPathGraphNodes(final HashMap<String, Block> blocks, ArrayList<CoarsePathSegment> coarsePathSegments) {
        HashMap<Block, BlockNode> blockNodes = new HashMap<>();
        for (Block b : blocks.values()) {
            blockNodes.put(b, new BlockNode(b));
        }

        for (CoarsePathSegment cp1 : coarsePathSegments) {
            Block cp1SourceBlock = cp1.getSourceBlock();
            Block cp1SinkBlock = cp1.getSinkBlock();

            // internal connection
            if (cp1SourceBlock == cp1SinkBlock) {
                continue;
            }

            blockNodes.get(cp1SourceBlock).getImmediateDownstreamBlocks().add(blockNodes.get(cp1SinkBlock));
            blockNodes.get(cp1SinkBlock).getImmediateUpstreamBlocks().add(blockNodes.get(cp1SourceBlock));

//            for (CoarsePathSegment cp2: coarsePathSegments) {
//                Block cp2SourceBlock = cp2.getSourceBlock();
//                Block cp2SinkBlock = cp2.getSinkBlock();
//
//                // internal connection
//                if (cp2SourceBlock == cp2SinkBlock) {
//                    continue;
//                }
//
//                // cp1 is before cp2
//                // add edge
//                if (cp1SinkBlock == cp2SourceBlock) {
//                    blockNodes.get(cp1SinkBlock).getImmediateDownstreamBlocks().add(blockNodes.get(cp2SourceBlock));
//                    blockNodes.get(cp2SourceBlock).getImmediateUpstreamBlocks().add(blockNodes.get(cp1SinkBlock));
//                } else if (cp2SinkBlock == cp1SourceBlock) {
//                    blockNodes.get(cp2SinkBlock).getImmediateDownstreamBlocks().add(blockNodes.get(cp1SourceBlock));
//                    blockNodes.get(cp1SourceBlock).getImmediateUpstreamBlocks().add(blockNodes.get(cp2SinkBlock));
//                }
//            }
        }

        return blockNodes;
    }

    /**
     * build all path segments from netlist
     * @param netlist global netlist
     * @return all coarse path segments
     */
    public static ArrayList<CoarsePathSegment> buildCoarsePathSegements(CoarseNetlist netlist) {
        ArrayList<CoarsePathSegment> cps = new ArrayList<>();
        for (CoarseNet cn : netlist.getNets()) {
            flattenNetToPathSegments(cn, cps);
        }

        return cps;
    }

    /**
     * flattens a net (tree) into paths (linear, parallel)
     * @param cn net
     * @param cps resulting path segments
     */
    public static void flattenNetToPathSegments(CoarseNet cn, ArrayList<CoarsePathSegment> cps) {
        for (NetNode sink : cn.sinks) {
            CoarsePathSegment cp = new CoarsePathSegment();
            cp.setSource(cn.source);
            cp.setSink(sink);
            cps.add(cp);
        }
    }

    /**
     * builds full block to block coarse paths from segments
     * @param g block nodes
     * @param el all path segments
     * @return all coarse paths
     */
    public static ArrayList<CoarsePath> buildCoarsePathsFromGraph(HashMap<Block, BlockNode> g, ArrayList<CoarsePathSegment> el) {
        ArrayList<CoarsePath> cps = new ArrayList<>();
        for (BlockNode b : g.values()) {
            if (b.getBlock().isGated()) {
                ArrayList<ArrayList<CoarsePathSegment>> dsGatedPaths = new ArrayList<>();
                generateDsPathRecH(b, el, null, dsGatedPaths);

                for (ArrayList<CoarsePathSegment> cpa : dsGatedPaths) {
                    CoarsePath cp = new CoarsePath();
                    cp.getPathSegments().addAll(cpa);
                    b.getDownstreamPaths().add(cp);
                    cps.add(cp);
                }
            }
        }

        return cps;
    }

    /**
     * helper function to recursively assemble parallel paths
     *
     * this function serves as the entry point to the actual recursive function by modifying the initial
     * terminating conditions
     * @param curBlk current node
     * @param allCoarsePaths all coarse path segments
     * @param curPath the build path so far
     * @param allGatedPaths all paths build on this sub path
     */
    public static void generateDsPathRecH(
            BlockNode curBlk,
            ArrayList<CoarsePathSegment> allCoarsePaths,
            ArrayList<CoarsePathSegment> curPath,
            ArrayList<ArrayList<CoarsePathSegment>> allGatedPaths) {
        if (curBlk.getImmediateDownstreamBlocks().size() == 0) {
            return;
        }

        for (BlockNode dsBn : curBlk.getImmediateDownstreamBlocks()) {
            for (CoarsePathSegment cps : allCoarsePaths) {
                if (cps.getSourceBlock() == curBlk.getBlock() && cps.getSinkBlock() == dsBn.getBlock()) {
                    ArrayList<CoarsePathSegment> newCurPath = ((curPath == null)
                            ? new ArrayList<>()
                            : new ArrayList<>(curPath));
                    newCurPath.add(cps);
                    generateDsPathRec(dsBn, allCoarsePaths, newCurPath, allGatedPaths);
                }
            }
        }
    }

    /**
     * helper function to recursively assemble parallel paths
     *
     * {@link #generateDsPathRecH} as the entry point you probably want to use
     * @param curBlk current node
     * @param allCoarsePaths all coarse path segments
     * @param curPath the build path so far
     * @param allGatedPaths all paths build on this sub path
     */
    public static void generateDsPathRec(
            BlockNode curBlk,
            ArrayList<CoarsePathSegment> allCoarsePaths,
            ArrayList<CoarsePathSegment> curPath,
            ArrayList<ArrayList<CoarsePathSegment>> allGatedPaths) {
        if (curBlk.getImmediateDownstreamBlocks().size() == 0 || curBlk.getBlock().isGated()) {
            allGatedPaths.add(curPath);
            return;
        }

        for (BlockNode dsBn : curBlk.getImmediateDownstreamBlocks()) {
            for (CoarsePathSegment cps : allCoarsePaths) {
                if (cps.getSourceBlock() == curBlk.getBlock() && cps.getSinkBlock() == dsBn.getBlock()) {
                    ArrayList<CoarsePathSegment> newCurPath = curPath == null
                            ? new ArrayList<>()
                            : new ArrayList<>(curPath);
                    newCurPath.add(cps);
                    generateDsPathRec(dsBn, allCoarsePaths, newCurPath, allGatedPaths);
                }
            }
        }
    }
}
