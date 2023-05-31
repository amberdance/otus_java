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
import java.util.Objects;

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
        var methods = getMethods(initialConfigClass);
        var instance = instantiate(initialConfigClass);
        invokeMethods(methods, instance);
    }

    private void invokeMethods(List<Method> methods, Object instance) {
        for (var method : methods) {
            var name =
                    method.getAnnotation(COMPONENT_ANNOTATION_CLASS).name();

            if (appComponentsByName.containsKey(name)) {
                throw new RuntimeException(String.format(
                        "Duplicate component name conflict, please rename the given component -> %s",
                        name));
            }

            try {
                var component =
                        method.invoke(instance, injectComponentParams(method));
                appComponents.add(component);
                appComponentsByName.put(name, component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object[] injectComponentParams(Method method) {
        if (method.getParameterCount() == 0) {
            return null;
        }

        var params = method.getParameters();
        var dependencies = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            dependencies[i] = getAppComponent(params[i].getType());
        }

        return dependencies;
    }

    private List<Method> getMethods(Class<?> initialConfigClass) {
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

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        Objects.requireNonNull(componentClass);

        var components =
                appComponents.stream().filter(componentClass::isInstance)
                        .toList();
        var len = components.size();

        if (len == 0) {
            throw new RuntimeException("Cannot find a suitable dependency");
        }

        if (len > 1) {
            throw new RuntimeException(
                    "Only one instance of a component can exist");
        }

        return ((C) components.get(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var comp = ((C) appComponentsByName.get(componentName));
        Objects.requireNonNull(comp);

        return comp;
    }
}
