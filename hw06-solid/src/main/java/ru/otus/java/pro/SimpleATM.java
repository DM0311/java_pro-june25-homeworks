package ru.otus.java.pro;

import java.util.*;
import ru.otus.java.pro.exceptions.IncorrectSumException;
import ru.otus.java.pro.exceptions.NotEnoughMoneyException;

public class SimpleATM implements ATM {
    private NavigableMap<Banknote, Integer> bucket = new TreeMap<>(Comparator.comparing(Banknote::getNominal));

    public SimpleATM(List<Banknote> banknotes) {
        deposit(banknotes);
    }

    @Override
    public void deposit(List<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            bucket.merge(banknote, 1, Integer::sum);
        }
    }

    @Override
    public List<Banknote> withdraw(long sum) {
        if (sum <= 0) {
            throw new IncorrectSumException();
        }
        if (sum > getBalance()) {
            throw new NotEnoughMoneyException();
        }
        List<Banknote> cash = new ArrayList<>();
        Map<Banknote, Integer> suitableBanknote = findSuitableBanknotes(sum);
        for (Map.Entry<Banknote, Integer> entry : suitableBanknote.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                cash.add(entry.getKey());
            }
            bucket.merge(entry.getKey(), -entry.getValue(), Integer::sum);
        }
        return cash;
    }

    @Override
    public long getBalance() {
        long sumCounter = 0;
        for (Banknote banknote : bucket.keySet()) {
            sumCounter += (long) bucket.get(banknote) * banknote.getNominal();
        }
        return sumCounter;
    }

    private Map<Banknote, Integer> findSuitableBanknotes(long neededSum) {
        long restSum = neededSum;
        Map<Banknote, Integer> suitableBanknotes = new HashMap<>();
        for (Map.Entry<Banknote, Integer> entry : bucket.descendingMap().entrySet()) {
            Banknote banknote = entry.getKey();
            Integer banknoteCount = entry.getValue();

            final int neededCountBanknotes = (int) (restSum / banknote.getNominal());
            if (neededCountBanknotes > 0 && neededCountBanknotes <= banknoteCount) {
                suitableBanknotes.put(banknote, neededCountBanknotes);
                restSum -= (long) neededCountBanknotes * banknote.getNominal();
            }
        }
        if (restSum != 0) {
            throw new NotEnoughMoneyException();
        }
        return suitableBanknotes;
    }
}
