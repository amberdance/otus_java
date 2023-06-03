package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TestLoggingProxy implements InvocationHandler {

    private final Object original;

    private final TestLoggingInterface proxy;
    private final Set<String> methodsWithLogAnnotation = new HashSet<>();


    public TestLoggingProxy() {
        original = new TestLogging();
        proxy = (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingInterface.class.getClassLoader(),
                new Class[]{TestLoggingInterface.class}, this);
        collectMethodsWithLogAnnotation();
    }

    private void collectMethodsWithLogAnnotation() {
        for (Method method : original.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                methodsWithLogAnnotation.add(getMethodNameWithParams(method.toString()));
            }
        }
    }

    private String getMethodNameWithParams(String methodName) {
        return methodName.replaceAll("(.*)\\.", "");
    }

    public TestLoggingInterface getProxy() {
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invokeResult = method.invoke(original, args);
        System.out.println(method);
        if (hasLogAnnotation(method.toString())) {
            printLog(method, argsAsString(args));
        }

        return invokeResult;
    }

    private boolean hasLogAnnotation(String methodName) {
        return methodsWithLogAnnotation.contains(getMethodNameWithParams(methodName));
    }

    private void printLog(Method method, String args) {
        System.out.printf("executed method: %s, param: %s%n", method.getName(), args);
    }

    private String argsAsString(Object[] args) {
        return Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));
    }

}
