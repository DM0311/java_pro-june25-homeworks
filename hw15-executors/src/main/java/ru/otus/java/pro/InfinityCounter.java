package ru.otus.java.pro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityCounter {
    private static final Logger logger = LoggerFactory.getLogger(InfinityCounter.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private int lastNumber = 1;
    private int currentNumber = 1;
    private int increment = 1;

    public static void main(String[] args) {
        InfinityCounter counter = new InfinityCounter();
        counter.executor.execute(counter::count);
        counter.executor.execute(counter::echo);
    }

    private synchronized void count() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while (currentNumber != lastNumber) {
                    this.wait();
                }
                logger.info(String.valueOf(currentNumber));
                if (currentNumber == 10) {
                    increment = -1;
                } else if (currentNumber == 1) {
                    increment = 1;
                }
                currentNumber += increment;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private synchronized void echo() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                // поэтому не if
                while (currentNumber == lastNumber) {
                    this.wait();
                }

                logger.info(String.valueOf(lastNumber));
                lastNumber = currentNumber;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
