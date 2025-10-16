package ru.otus.java.pro.services;

import ru.otus.java.pro.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
