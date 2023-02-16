package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestLoggingProxyOld implements InvocationHandler {

    private final Object original;

    private final TestLoggingInterface proxy;

    public TestLoggingProxyOld() {
        original = new TestLogging();
        proxy = (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingInterface.class.getClassLoader(),
                new Class[]{TestLoggingInterface.class}, this);
    }


    public TestLoggingInterface getProxy() {
        return proxy;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invokeResult = method.invoke(original, args);

        if (hasLogAnnotation(method)) printLog(method, argsAsString(args));

        return invokeResult;
    }

    private boolean hasLogAnnotation(Method method) throws NoSuchMethodException {
        return original.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class);
    }

    private void printLog(Method method, String args) {
        System.out.printf("executed method: %s, param: %s%n", method.getName(), args);
    }

    private String argsAsString(Object[] args) {
        return Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));
    }


}
