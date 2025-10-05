package ru.otus.java.pro;

import ru.otus.java.pro.proxy.LogPrinter;
import ru.otus.java.pro.source.Calculation;
import ru.otus.java.pro.source.CalculationImpl;

public class Test {
    public static void main(String[] args) {
        Calculation myCalc = LogPrinter.createCalculation(new CalculationImpl());
        myCalc.calculation(1);
        myCalc.calculation(2, 4);
        myCalc.calculation(1, 4, "HelloParam");
    }
}
