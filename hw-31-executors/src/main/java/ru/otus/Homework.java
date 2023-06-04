package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Homework {

    private static final Logger log = LoggerFactory.getLogger(Homework.class);
    private static final int ITERATIONS = 10;
    private static int currentValue = 1;
    private boolean isFirstThread = false;


    public void run() {
        new Thread(() -> runLoop(true), "Thread-1").start();
        new Thread(() -> runLoop(false), "Thread-2").start();
    }

    private synchronized void runLoop(boolean first) {
        while (!Thread.currentThread().isInterrupted()) {
            boolean isAscendingLoop = currentValue != ITERATIONS - 1;

            for (int i = (isAscendingLoop ? 1 : ITERATIONS);
                 i != (isAscendingLoop ? ITERATIONS : 1);
                 i += (isAscendingLoop ? 1 : -1)) {

                try {
                    while (first == isFirstThread) {
                        this.wait();
                    }

                    isFirstThread = first;
                    currentValue = i;
                    log.info(String.valueOf(i));
                    sleep(100);

                    this.notify();
                } catch (InterruptedException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
