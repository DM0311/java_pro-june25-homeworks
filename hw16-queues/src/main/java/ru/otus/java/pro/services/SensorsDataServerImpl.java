package ru.otus.java.pro.services;

import ru.otus.java.pro.api.SensorsDataChannel;
import ru.otus.java.pro.api.SensorsDataServer;
import ru.otus.java.pro.api.model.SensorData;

public class SensorsDataServerImpl implements SensorsDataServer {

    private final SensorsDataChannel sensorsDataChannel;

    public SensorsDataServerImpl(SensorsDataChannel sensorsDataChannel) {
        this.sensorsDataChannel = sensorsDataChannel;
    }

    @Override
    public void onReceive(SensorData sensorData) {
        sensorsDataChannel.push(sensorData);
    }
}
