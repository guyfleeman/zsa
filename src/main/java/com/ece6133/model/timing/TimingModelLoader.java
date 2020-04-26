package com.ece6133.model.timing;

import java.io.File;

public class TimingModelLoader {
    public static TimingModel loadTimingModel(String model) {
        return TimingModelLoader.loadTimingModel(new File(model));
    }

    public static TimingModel loadTimingModel(File model) {
        return null;
    }
}
