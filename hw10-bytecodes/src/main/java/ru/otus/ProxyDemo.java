package ru.otus;

public class ProxyDemo {

    public static void main(String[] args) throws NoSuchMethodException {
        var log = new TestLoggingProxy().getProxy();

        log.calculation(1);
        log.calculation(1, 2);
        log.calculation(1, 2, 3);
        log.calculation(1, 2, 3, 4);
    }
}
