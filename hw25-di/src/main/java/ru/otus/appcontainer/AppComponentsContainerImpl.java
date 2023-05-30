package ru.otus.appcontainer;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.reflections.ReflectionUtils.Constructors;
import static org.reflections.ReflectionUtils.Methods;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final static Logger log = LoggerFactory.getLogger(
            AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    private final Class<AppComponent> COMPONENT_ANNOTATION_CLASS =
            AppComponent.class;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        processInjectDependencies(configClass);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(
                AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(
                    String.format("Given class is not config %s",
                            configClass.getName()));
        }
    }

    private void processInjectDependencies(
            Class<?> initialConfigClass) {
        var components = getComponents(initialConfigClass);
        var instance = instantiate(initialConfigClass);

        invokeMethodsWithoutParams(components, instance);
        invokeMethodsWithParams(components, instance);


        return;
    }

    private List<Method> getComponents(Class<?> initialConfigClass) {
        var components = ReflectionUtils.get(Methods.of(initialConfigClass))
                .stream()
                .filter(method -> method.isAnnotationPresent(
                        COMPONENT_ANNOTATION_CLASS)).
                sorted(Comparator.comparingInt(method -> method.getAnnotation(
                        COMPONENT_ANNOTATION_CLASS).order()))
                .toList();

        if (components.isEmpty()) {
            throw new RuntimeException(
                    "The components could not be loaded, perhaps you forgot to annotate them @" +
                            COMPONENT_ANNOTATION_CLASS + " ?");
        }

        log.debug("Found components: {}", components);
        return components;
    }

    private Object instantiate(Class<?> initialConfigClass) {
        try {
            var instance =
                    ReflectionUtils.get(Constructors.of(initialConfigClass))
                            .stream()
                            .findFirst()
                            .orElseThrow()
                            .newInstance();
            log.debug("Instance created: {}", instance);
            return instance;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException("Cannot instantiate the given class: " +
                    initialConfigClass);
        }
    }


    private void invokeMethodsWithoutParams(List<Method> components,
                                            Object instance) {
        var componentsWithoutParams = components.stream()
                .filter(method -> method.getParameters().length == 0).toList();

        for (var component : componentsWithoutParams) {
            var name = getComponentNameByAnnotation(component);
            var dependency = ReflectionUtils.invoke(component, instance);
            appComponents.add(dependency);
            appComponentsByName.put(name, dependency);
        }
    }

    private void invokeMethodsWithParams(List<Method> components,
                                         Object instance) {
        var componentsWithoutParams = components.stream()
                .filter(method -> method.getParameters().length > 0).toList();

        for (var component : componentsWithoutParams) {
            var name = getComponentNameByAnnotation(component);
            var params = component.getParameters();
            var injectComponents = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                if (params[i].getType()
                        .isAssignableFrom(appComponents.get(i).getClass())) {
                    injectComponents[i] = appComponents.get(i);
                }
            }

            var dependency = ReflectionUtils.invoke(component, instance,
                    injectComponents);
            appComponents.add(dependency);
            appComponentsByName.put(name, dependency);
        }
    }


    private String getComponentNameByAnnotation(Method comp) {
        return comp.getAnnotation(COMPONENT_ANNOTATION_CLASS).name();
    }


    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(p -> componentClass.isAssignableFrom(p.getClass()))
                .findFirst().orElseThrow();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return null;
    }
}
