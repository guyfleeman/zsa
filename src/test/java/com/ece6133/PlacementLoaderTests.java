package com.ece6133;

import com.ece6133.model.timing.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlacementLoaderTests {
    /**
     * generate a small scale test fake block
     * @param name name
     * @param gated gated
     * @return fake block
     */
    public Block makeFakeBlock(String name, boolean gated) {
        return makeFakeBlock(name, gated, 0, 0);
    }

    /**
     * generate a small scale test fake block
     * @param name name
     * @param gated gated
     * @param x loc
     * @param y loc
     * @return fake block
     */
    public Block makeFakeBlock(String name, boolean gated, int x, int y) {
        Block b = new Block();
        b.setName(name);
        b.setGated(gated);
        PlacementInfo pli = new PlacementInfo();
        pli.name = "PLI - " + name;
        pli.x = x;
        pli.y = y;
        b.setPlacementInfo(pli);
        return b;
    }

    /**
     * add a fake net
     * @param name name
     * @param output driver
     * @param input sinks
     * @param blocks block db
     */
    public void addNet(String name, String output, String[] input, HashMap<String, Block> blocks) {
        ArrayList<String> names = new ArrayList<>();
        names.add(name);
        blocks.get(output).getOutputs().put(name, names);

        for (String i: input) {
            ArrayList<String> iNames = new ArrayList<>();
            iNames.add(name);
            blocks.get(i).getInputs().put(name, iNames);
        }
    }

    /**
     * make small scale test graph
     * @return test graph
     */
    public K6DesignModel getTestGraph() {
        String[] names = {"A", "A1", "B", "B1", "B2", "C", "C1", "D", "E", "F"};
        HashMap<String, Block> blocks = new HashMap<>();
        for (String n: names) {
            blocks.put(n, makeFakeBlock(n, false));
        }

        blocks.get("A").setGated(true);
        blocks.get("B").setGated(true);
        blocks.get("C").setGated(true);
        blocks.get("D").setGated(true);
        blocks.get("E").setGated(true);
        blocks.get("F").setGated(true);

        blocks.get("A").setDelay(2);
        blocks.get("B").setDelay(3);
        blocks.get("C").setDelay(5);
        blocks.get("D").setDelay(1);
        blocks.get("E").setDelay(3);
        blocks.get("F").setDelay(4);

        blocks.get("D").setRequiredTime(50);
        blocks.get("E").setRequiredTime(50);
        blocks.get("F").setRequiredTime(50);

        addNet("a-out", "A", new String[]{"A1", "B1"}, blocks);
        addNet("b-out", "B", new String[]{"A1", "B1"}, blocks);
        addNet("c-out", "C", new String[]{"C1", "B1"}, blocks);
        addNet("a1-out", "A1", new String[]{"D"}, blocks);
        addNet("b1-out", "B1", new String[]{"B2"}, blocks);
        addNet("b2-out", "B2", new String[]{"D", "E"}, blocks);
        addNet("c1-out", "C1", new String[]{"F"}, blocks);

        K6DesignModel dm = new K6DesignModel();
        dm.setBlocks(blocks);
        return dm;
    }

    /**
     * test small scale primitive netlist recovery
     */
    @Test
    public void primitiveNetlist() {
        K6DesignModel dm = getTestGraph();

        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        System.out.println(cnl);
        assert(cnl.getNets().size() == 7);
    }

    /**
     * test small scale path recovery
     */
    @Test
    public void primitiveCoarsePathList() {
        K6DesignModel dm = getTestGraph();

        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        CoarsePathList cpl = K6DesignModelLoader.recoverPaths(cnl, dm.getBlocks());
        System.out.println(cpl);
    }

    /**
     * test large scale placement
     * @throws IOException
     */
    @Test
    public void parsePlacement() throws IOException {
        File rsc = new File("src/test/resources/fir.place");
        System.out.println(rsc.getAbsoluteFile());
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(rsc);

        for (PlacementInfo p: plInfo.values()) {
            assert(p != null);
        }
        assert(plInfo.size() == 1141);
    }

    /**
     * test largescale netlist parsing
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     */
    @Test
    public void loadNetlist() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));
        //for (String k: blocks.keySet()) {
            //System.out.println(blocks.get(k));
        //}

        for (Block b: blocks.values()) {
            assert(b != null);
        }
    }

    /**
     * load a large model
     * @return
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public K6DesignModel loadModel() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(new File("src/test/resources/fir.place"));
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));

        K6DesignModel k6dm = new K6DesignModel();
        k6dm.setPlInfo(plInfo);
        k6dm.setBlocks(blocks);

        return k6dm;
    }

    /**
     * test large scale placement mapping
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void testPlacementMapping() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
    }

    /**
     * test large scale netlist construction
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @Test
    public void testNetlistBuild() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        System.out.println("Recovered Nets:" + cnl.getNets().size());
        System.out.println(cnl);

        ArrayList<CoarseNet> badNets = new ArrayList<>();
        for (CoarseNet cn: cnl.getNets()) {
            if (cn.sinks.size() == 0) {
                badNets.add(cn);
            }
        }

        System.out.println("Bad Nets: " + badNets.size());
        for (CoarseNet bn: badNets) {
            System.out.println(bn);
        }

        // invisible crossbar nets
        assert(badNets.size() == 151);
    }

    /**
     * test largescale path recovery
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws IOException
     */
    @Test(expected = StackOverflowError.class)
    public void testPathRec() throws SAXException, ParserConfigurationException, XPathExpressionException, IOException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        CoarsePathList cpl = K6DesignModelLoader.recoverPaths(cnl, dm.getBlocks());
        System.out.println("Recovered Paths:" + cpl);
    }
}
