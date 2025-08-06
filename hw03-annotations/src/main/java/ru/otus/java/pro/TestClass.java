package ru.otus.java.pro;

import ru.otus.java.pro.annotations.After;
import ru.otus.java.pro.annotations.Before;
import ru.otus.java.pro.annotations.Test;

public class TestClass {

    @Before
    public void methodOne() {
        throw new RuntimeException("Test failed!");
    }

    @Before
    public void methodTwo() {
        System.out.println("Test finished successfully!");
    }

    @Before
    public void methodThree() {
        System.out.println("Test finished successfully!");
    }

    @After
    public void methodFour() {
        throw new RuntimeException("Test failed!");
    }

    @After
    public void methodFive() {
        System.out.println("Test finished successfully!");
    }

    @Test
    public void methodSix() {
        System.out.println("Test finished successfully!");
    }
}
