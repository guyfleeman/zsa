package com.ece6133.model.arch;

import java.util.ArrayList;

public interface Arch {
    public ArrayList<String> getSupportedSubcktTypes();

    public boolean supportsSubckt(final String subcktName);
}
