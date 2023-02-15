package ru.otus.test;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.reflect.Modifier;


public class TestMethodsInitializer implements Initializer {
    private final MethodContainer methodContainer;

    public TestMethodsInitializer(MethodContainer methodContainer) {
        this.methodContainer = methodContainer;
    }

    @Override
    public void init() {
        checkIsMethodsAvailable();
        checkIsModifiersCorrect();
        checkIsMethodsAnnotated();
    }


    private void checkIsMethodsAvailable() {
        if (methodContainer.getMethods().isEmpty())
            throw new RuntimeException(ErrorMessage.NO_METHODS_DECLARED.value());
    }

    private void checkIsModifiersCorrect() {
        StringBuffer illegalMethods = new StringBuffer();

        methodContainer.getMethods().forEach(method -> {
            int index = 0;

            if (method.getModifiers() == Modifier.STATIC && (method.isAnnotationPresent(Before.class)
                    || method.isAnnotationPresent(After.class)
                    || method.isAnnotationPresent(Test.class)))
                illegalMethods.append(String.format("\n %d) %s", ++index, method));
        });

        if (!illegalMethods.isEmpty())
            throw new RuntimeException(String.format("%s:'%s'", ErrorMessage.STATIC_METHOD_DECLARED.value(),
                    illegalMethods));
    }

    private void checkIsMethodsAnnotated() {
        if (methodContainer.getMethods().stream().anyMatch(method -> method.getAnnotations().length == 0)) {
            throw new RuntimeException(ErrorMessage.NO_ANNOTATIONS_DECLARED.value());
        }
    }
}
