package ru.otus.java.pro.api;

import ru.otus.java.pro.api.model.SensorData;

public interface SensorDataProcessor {
    void process(SensorData data);

    default void onProcessingEnd() {}
}
