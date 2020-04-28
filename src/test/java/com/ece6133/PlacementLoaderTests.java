package com.ece6133;

import com.ece6133.model.timing.K6DesignModelLoader;
import com.ece6133.model.timing.PlacementInfo;
import com.ece6133.model.timing.Block;
import com.ece6133.model.timing.CoarseNetlist;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PlacementLoaderTests {


    @Test
    public void parsePlacement() throws IOException {
        File rsc = new File("src/test/resources/fir.place");
        System.out.println(rsc.getAbsoluteFile());
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(rsc);

        //System.out.println(plInfo.size());

        assert(plInfo.size() == 1141);
    }

    @Test
    public void loadNetlist() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));
        for (String k: blocks.keySet()) {
            //System.out.println(blocks.get(k));
        }
    }

    @Test
    public void testPlacementMapping() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(new File("src/test/resources/fir.place"));
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));
        K6DesignModelLoader.mapPlacements(blocks, plInfo);
        for (String k: blocks.keySet()) {
            System.out.println(blocks.get(k));
        }
    }

    @Test
    public void testNetlistBuild() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HashMap<String, PlacementInfo> plInfo = K6DesignModelLoader.parsePlacementFile(new File("src/test/resources/fir.place"));
        HashMap<String, Block> blocks = K6DesignModelLoader.loadNetBlocks(new File("src/test/resources/fir.net"));
        K6DesignModelLoader.mapPlacements(blocks, plInfo);
        CoarseNetlist cnl = K6DesignModelLoader.buildCoarseNetlist(blocks);
        System.out.println("Recovered Nets:" + cnl.getNets().size());
    }
}
