package ru.otus.java.pro.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.reflections.Reflections;
import ru.otus.java.pro.appcontainer.api.AppComponent;
import ru.otus.java.pro.appcontainer.api.AppComponentsContainer;
import ru.otus.java.pro.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(String path) {
        Reflections reflections = new Reflections(path);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        for (Class<?> cls : typesAnnotatedWith) {
            processConfig(cls);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        List<Method> confMethods = new ArrayList<>();
        for (Method method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                confMethods.add(method);
            }
        }
        confMethods.sort(Comparator.comparingInt(
                method -> method.getAnnotation(AppComponent.class).order()));
        confMethods.forEach(method -> createBean(configInstance, method));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C target = null;
        for (Object bean : appComponents) {
            if (componentClass.isAssignableFrom(bean.getClass())) {
                if (target != null) {
                    throw new IllegalStateException(
                            "More than one component found for type: " + componentClass.getName());
                }
                target = (C) bean;
            }
        }
        if (target == null) {
            throw new IllegalStateException("No component found for type: " + componentClass.getName());
        }
        return target;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object bean = appComponentsByName.get(componentName);
        if (bean == null) {
            throw new NoSuchElementException(String.format("Bean with name %s not found.", componentName));
        }
        return (C) bean;
    }

    private void createBean(Object configInstance, Method creationMethod) {
        AppComponent metaData = creationMethod.getAnnotation(AppComponent.class);
        String name = metaData.name();
        if (name == null) {
            throw new IllegalArgumentException("AppComponent name parameter must not be empty");
        }
        if (appComponentsByName.containsKey(name)) {
            throw new IllegalStateException(
                    String.format("Duplicate name: AppComponent with name % already defined in config", name));
        }

        Class<?>[] parameterTypes = creationMethod.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getAppComponent(parameterTypes[i]);
        }

        try {
            Object bean = creationMethod.invoke(configInstance, args);
            appComponents.add(bean);
            appComponentsByName.put(name, bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
