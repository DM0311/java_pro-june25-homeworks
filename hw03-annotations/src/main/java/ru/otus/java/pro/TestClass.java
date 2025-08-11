package ru.otus.java.pro;

import ru.otus.java.pro.annotations.After;
import ru.otus.java.pro.annotations.Before;
import ru.otus.java.pro.annotations.Test;

public class TestClass {

    @Before
    public void methodOne() {}

    @Before
    public void methodTwo() {}

    @Before
    public void methodThree() {}

    @After
    public void methodFour() {
        throw new RuntimeException("Test failed!");
    }

    @After
    public void methodFive() {}

    @Test
    public void methodSix() {}

    @Test
    public void methodSeven() {}

    @Test
    public void methodEight() {
        throw new RuntimeException("Test failed!");
    }
}
