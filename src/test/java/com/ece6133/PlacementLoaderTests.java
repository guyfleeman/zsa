package com.ece6133;

import com.ece6133.model.timing.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlacementLoaderTests {
    public void getTestGraph() {

    }

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

    public K6DesignModel loadModel() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(new File("src/test/resources/fir.place"));
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));

        K6DesignModel k6dm = new K6DesignModel();
        k6dm.setPlInfo(plInfo);
        k6dm.setBlocks(blocks);

        return k6dm;
    }

    @Test
    public void testPlacementMapping() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
    }

    @Test
    public void testNetlistBuild() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        System.out.println("Recovered Nets:" + cnl.getNets().size());
    }

    @Test
    public void testPathRec() throws SAXException, ParserConfigurationException, XPathExpressionException, IOException {
        K6DesignModel dm = loadModel();
        K6DesignModelLoader.mapPlacements(dm.getBlocks(), dm.getPlInfo());
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(dm.getBlocks());
        CoarsePathList cpl = K6DesignModelLoader.recoverPaths(cnl, dm.getBlocks());
        System.out.println("Recovered Paths:" + cpl);
    }
}
