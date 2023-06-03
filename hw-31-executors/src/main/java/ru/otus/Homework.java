package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Homework {

    private static final Logger log = LoggerFactory.getLogger(Homework.class);
    private static final int ITERATIONS = 10;
    private static int currentValue = 1;
    private boolean isFirstThread = false;


    public void run() {
        var t1 = new Thread(() -> runLoop(true), "Thread-1");
        var t2 = new Thread(() -> runLoop(false), "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private void runLoop(boolean first) {
        while (true) {
            boolean isAscendingLoop = currentValue != ITERATIONS - 1;

            for (int i = (isAscendingLoop ? 1 : ITERATIONS);
                 i != (isAscendingLoop ? ITERATIONS : 1);
                 i += (isAscendingLoop ? 1 : -1)) {

                synchronized (this) {
                    printNumbers(i, first);
                }

            }
        }
    }

    private void printNumbers(int i, boolean first) {
        try {
            while (first == isFirstThread) {
                this.wait();
            }

            log.info(String.valueOf(i));
            isFirstThread = first;
            currentValue = i;

            this.notify();
            Thread.sleep(666);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
