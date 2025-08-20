package ru.otus.java.pro.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException() {}

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
