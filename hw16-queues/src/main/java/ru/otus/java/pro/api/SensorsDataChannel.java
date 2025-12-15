package ru.otus.java.pro.api;

import java.util.concurrent.TimeUnit;
import ru.otus.java.pro.api.model.SensorData;

public interface SensorsDataChannel {
    boolean push(SensorData sensorData);

    boolean isEmpty();

    SensorData take(long timeout, TimeUnit unit) throws InterruptedException;
}
