package ru.otus.java.pro;

import ru.otus.java.pro.handlers.CustomAnnotationHandler;

public class Main {
    public static void main(String[] args) {
        CustomAnnotationHandler handler = new CustomAnnotationHandler();
        handler.runTests(TestClass.class);
    }
}
