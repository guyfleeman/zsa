package com.ece6133.model.arch.k6_n10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * loads the architecture definition
 *
 * includes timing and placement config, subckt defs, and IO
 */
public class K6ArchLoader {
    /**
     * loads the arch definition
     * @param archFile arch file
     * @return the architecture definition
     * @throws ParserConfigurationException unreachable
     * @throws IOException file errors
     * @throws SAXException arch def file XML error
     */
    public static K6Arch loadArch(final String archFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document archDef = builder.parse(new File(archFile));
        archDef.getDocumentElement().normalize();

        Element root = archDef.getDocumentElement();
        K6Arch archModel = new K6Arch();

        decomposeRoot(root, archModel);

        return archModel;
    }

    /**
     * breaks down the root element
     * @param root root el
     * @param archModel model
     */
    private static void decomposeRoot(final Element root, K6Arch archModel) {
        NodeList models = root.getElementsByTagName("models");
        decomposeModels((Element) models.item(0), archModel);
    }

    /**
     * recover models
     * @param modelsRoot models element root
     * @param archModel model
     */
    private static void decomposeModels(final Element modelsRoot, K6Arch archModel) {
        NodeList models = modelsRoot.getElementsByTagName("model");
        for (int i = 0; i < models.getLength(); i++) {
            addModel((Element) models.item(i), archModel);
        }
    }

    /**
     * parses and loads a subckt model
     * @param model model el
     * @param archModel model
     */
    private static void addModel(final Element model, K6Arch archModel) {
        K6SubcktModel newModel = new K6SubcktModel();

        final String name = model.getAttribute("name");
        newModel.setName(name);

        ArrayList<String> inputPortNames = new ArrayList<>();
        NodeList inputPortNodes = ((Element) model.getElementsByTagName("input_ports").item(0))
                .getElementsByTagName("port");
        for (int i = 0; i < inputPortNodes.getLength(); i++) {
            inputPortNames.add(((Element) inputPortNodes.item(i)).getAttribute("name"));
        }
        newModel.getInputPortNames().addAll(inputPortNames);

        ArrayList<String> outputPortNames = new ArrayList<>();
        NodeList outputPortNodes = ((Element) model.getElementsByTagName("output_ports").item(0))
                .getElementsByTagName("port");
        for (int i = 0; i < inputPortNodes.getLength(); i++) {
            outputPortNames.add(((Element) outputPortNodes.item(i)).getAttribute("name"));
        }
        newModel.getOutputPortNames().addAll(outputPortNames);

        archModel.addModel(newModel);
    }
}
