package ru.otus.java.pro.exceptions;

public class IncorrectSumException extends RuntimeException {
    public IncorrectSumException() {}

    public IncorrectSumException(String message) {
        super(message);
    }
}
