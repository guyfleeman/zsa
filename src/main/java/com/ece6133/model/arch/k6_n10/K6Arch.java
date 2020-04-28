package com.ece6133.model.arch.k6_n10;

import com.ece6133.model.arch.Arch;

import java.util.ArrayList;

/**
 * architecture model for K6, includes subckt and timing
 */
public class K6Arch implements Arch {
    private ArrayList<K6SubcktModel> models = new ArrayList<>();

    public K6Arch() {}

    /**
     * adds a subckt model def
     * @param newModel model
     */
    public void addModel(K6SubcktModel newModel) {
        models.add(newModel);
    }

    /**
     * fetches a model by name
     * @param name name
     * @return model
     */
    public K6SubcktModel getModelByName(String name) {
        for (K6SubcktModel m: models) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }

    /**
     * get supported subckts (hard tile macros)
     * @return
     */
    @Override
    public ArrayList<String> getSupportedSubcktTypes() {
        return null;
    }

    /**
     * checks if a hardtile macro type is supported
     * @param subcktName macro name
     * @return supported by loaded arch
     */
    @Override
    public boolean supportsSubckt(String subcktName) {
        return true;
    }
}
