package ru.otus.java.pro.dataprocessor;

import java.util.List;
import java.util.Map;
import ru.otus.java.pro.model.Measurement;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
