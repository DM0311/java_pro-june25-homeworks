package ru.otus.java.pro;

import java.util.List;

public interface ATM {
    void deposit(List<Banknote> banknotes);

    List<Banknote> withdraw(long sum);

    long getBalance();
}
