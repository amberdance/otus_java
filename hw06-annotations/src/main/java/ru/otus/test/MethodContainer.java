package ru.otus.test;

import ru.otus.utils.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class MethodContainer {
    private final List<Method> methods;

    public MethodContainer(Object instance) {
        methods = Reflection.getMethods(instance, "main");
    }


    public Method[] getMethodByAnnotation(Class<? extends Annotation> type) {
        return getMethods().stream().filter(method -> method.isAnnotationPresent(type)).toArray
                (Method[]::new);
    }

    public List<Method> getMethods() {
        return methods;
    }

}
