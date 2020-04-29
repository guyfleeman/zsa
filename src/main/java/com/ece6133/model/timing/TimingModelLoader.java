package com.ece6133.model.timing;

import java.io.File;

public class TimingModelLoader {
    public static ZeroSlackTimingModel loadTimingModel(String model) {
        return TimingModelLoader.loadTimingModel(new File(model));
    }

    public static ZeroSlackTimingModel loadTimingModel(File model) {
        return null;
    }
}
