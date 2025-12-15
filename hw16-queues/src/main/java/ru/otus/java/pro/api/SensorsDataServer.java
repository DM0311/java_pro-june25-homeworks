package ru.otus.java.pro.api;

import ru.otus.java.pro.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
