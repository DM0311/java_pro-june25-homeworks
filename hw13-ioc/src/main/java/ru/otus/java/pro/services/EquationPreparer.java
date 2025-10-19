package ru.otus.java.pro.services;

import java.util.List;
import ru.otus.java.pro.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
