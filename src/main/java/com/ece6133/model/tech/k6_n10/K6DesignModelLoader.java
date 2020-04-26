package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.arch.k6_n10.K6Arch;
import com.ece6133.model.timing.Block;
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

public class K6DesignModelLoader {
    private K6DesignModelLoader() {}

    private enum ParseCtxMode {
        MODEL,
        INPUT,
        OUTPUT,
        LATCH,
        SUBCKT,
        LUT,
        NONE
    }

    public static K6DesignModel loadModel(final File modelBlif, final K6Arch arch) throws IOException {
        final BufferedReader fileReader = new BufferedReader(new FileReader(modelBlif));

        K6DesignModel model = new K6DesignModel();

        ParseCtxMode activeCtx = ParseCtxMode.NONE;
        String curLine;
        while ((curLine = fileReader.readLine()) != null) {
            if (curLine.equals("")) {
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

    private static void buildModelWithContext(final ParseCtxMode curCtx, K6DesignModel model, final String curLine) {
        switch (curCtx) {
            case MODEL:
                break;
            case INPUT: buildModelWithInputCtx(model, curLine); break;
            case OUTPUT: buildModelWithOutputCtx(model, curLine); break;
            case LATCH: buildModelWithLatchCtx(model, curLine); break;
            case SUBCKT: buildModelWithSubcktCtx(curLine); break;
            case LUT: buildModelWithLutCtx(curLine); break;
            default: throw new RuntimeException("unreachable");
        }
    }

    private static void buildModelWithInputCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
        for (String el: elements) {
            if (el.equalsIgnoreCase(".inputs")) {
                continue;
            } else if (el.equalsIgnoreCase("\\")) {
                return;
            } else {
                model.addInput(new NetNode(el));
            }
        }
    }

    private static void buildModelWithOutputCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
        for (String el: elements) {
            if (el.equalsIgnoreCase(".outputs")) {
                continue;
            } else if (el.equalsIgnoreCase("\\")) {
                return;
            } else {
                model.addInput(new NetNode(el));
            }
        }
    }

    private static void buildModelWithLatchCtx(K6DesignModel model, final String curLine) {
        final String[] elements = curLine.split(" ");
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

    private static void buildModelWithSubcktCtx(final String curLine) {
        SubcktBuilder.getInstance().appendDefLine(curLine);
    }

    private static void buildModelWithLutCtx(final String curLine) {
        LutBuilder.getInstance().appendDefLine(curLine);
    }

    public static void loadPlacement(final File netBlocks, final File placeFile, K6DesignModel model) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = parsePlacementFile(placeFile);
        HashMap<String, Block> blocks = loadNetBlocks(netBlocks);
        mapPlacements(blocks, plInfo);
    }

    public static HashMap<String, PlacementInfo> parsePlacementFile(final File placeFile) throws IOException {
        final BufferedReader fileReader = new BufferedReader(new FileReader(placeFile));
        HashMap<String, PlacementInfo> plInfo = new HashMap<>();
        String curLine;
        while ((curLine = fileReader.readLine()) != null) {
            if (curLine.equalsIgnoreCase("") || curLine.startsWith("#") || curLine.startsWith("Netlist_File") || curLine.startsWith("Array")) {
                continue;
            }

            String[] dirtyEls = curLine.split("\t");
            ArrayList<String> els = new ArrayList<>();
            for (String el: dirtyEls) {
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

            //System.out.println(newPlacement);

            plInfo.put(newPlacement.name, newPlacement);
        }

        return plInfo;
    }

    public static void mapPlacements(HashMap<String, Block> blocks, HashMap<String, PlacementInfo> plInfo) {
        for (String k: blocks.keySet()) {
            if (!plInfo.containsKey(k)) {
                throw new RuntimeException("key not found: " + k);
            }

            blocks.get(k).setPlacementInfo(plInfo.get(k));
        }
    }

    public static HashMap<String, Block> loadNetBlocks(final File netFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
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

            Element outputsEl = (Element) topLevelChildE.getElementsByTagName("outputs").item(0);
            addOutputs(outputsEl, tlBlock);

            blocks.put(tlBlock.getName(), tlBlock);
        }

        return blocks;
    }

    public static void addInputs(Element inputsEl, Block blk) {
        NodeList ports = inputsEl.getElementsByTagName("port");
        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            ArrayList<String> netNames = new ArrayList<>(Arrays.asList(port.getTextContent().split(" ")));
            blk.getInputs().put(portName, netNames);
        }
    }

    public static void addOutputs(Element outputsEl, Block blk) {
        NodeList ports = outputsEl.getElementsByTagName("port");
        for (int i = 0; i < ports.getLength(); i++) {
            Element port = (Element) ports.item(i);
            String portName = port.getAttribute("name");
            ArrayList<String> netNames = new ArrayList<>(Arrays.asList(port.getTextContent().split(" ")));
            blk.getOutputs().put(portName, netNames);
        }
    }
}
