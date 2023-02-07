package ru.otus.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Reflection {
    private Reflection() {
    }


    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    public static List<Method> getMethods(Object obj, String... excludeMethods) {
        if (excludeMethods.length == 0) return getMethods(obj);

        StringBuilder excludeString = new StringBuilder();

        for (String s : excludeMethods) {
            excludeString.append(s).append("|");
        }

        excludeString.insert(0, "^(").append(")");
        excludeString.deleteCharAt(excludeString.length() - 2);

        return Arrays.stream(obj.getClass().getDeclaredMethods()).filter(method -> !method.getName().matches(excludeString.toString())).toList();
    }

    public static List<Method> getMethods(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredMethods()).toList();
    }

    public static void invokeMethod(Method method, Object instance) throws InvocationTargetException,
            IllegalAccessException {
        method.setAccessible(true);
        method.invoke(instance);
    }

}