package ru.otus.java.pro.dataprocessor;

import java.util.List;
import ru.otus.java.pro.model.Measurement;

public interface Loader {

    List<Measurement> load();
}
