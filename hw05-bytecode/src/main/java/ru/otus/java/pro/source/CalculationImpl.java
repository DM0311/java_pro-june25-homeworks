package ru.otus.java.pro.source;

import ru.otus.java.pro.annotation.Log;

public class CalculationImpl implements Calculation {
    @Log
    @Override
    public void calculation(int param1) {}

    @Override
    public void calculation(int param1, int param2) {}

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {}
}
