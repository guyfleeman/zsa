package com.ece6133.model.arch.k6_n10;

import com.ece6133.model.arch.Arch;

import java.util.ArrayList;

public class K6Arch implements Arch {
    private ArrayList<K6Model> models = new ArrayList<>();

    public K6Arch() {}

    public void addModel(K6Model newModel) {
        models.add(newModel);
    }

    public K6Model getModelByName(String name) {
        for (K6Model m: models) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
    }

    @Override
    public ArrayList<String> getSupportedSubcktTypes() {
        return null;
    }

    @Override
    public boolean supportsSubckt(String subcktName) {
        return true;
    }
}
