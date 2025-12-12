package ru.otus.java.pro.lib;

import java.util.List;
import ru.otus.java.pro.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
