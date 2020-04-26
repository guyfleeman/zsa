package com.ece6133.model.tech.k6_n10;

import com.ece6133.model.arch.k6_n10.K6Arch;

import java.io.*;
import java.util.Arrays;

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

    public K6DesignModel loadModel(final File modelBlif, final K6Arch arch) throws IOException {
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

    private void buildModelWithContext(final ParseCtxMode curCtx, K6DesignModel model, final String curLine) {
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

    private void buildModelWithInputCtx(K6DesignModel model, final String curLine) {
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

    private void buildModelWithOutputCtx(K6DesignModel model, final String curLine) {
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

    private void buildModelWithLatchCtx(K6DesignModel model, final String curLine) {
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

    private void buildModelWithSubcktCtx(final String curLine) {
        SubcktBuilder.getInstance().appendDefLine(curLine);
    }

    private void buildModelWithLutCtx(final String curLine) {
        LutBuilder.getInstance().appendDefLine(curLine);
    }
}
