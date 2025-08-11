package ru.otus.java.pro.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAnnotationHandler {

    public void runTests(Class<?> clazz) {
        Map<String, List<Method>> methodsForTest = findMethods(clazz);
        executeTests(methodsForTest, clazz);
    }

    private Map<String, List<Method>> findMethods(Class<?> clazz) {
        Map<String, List<Method>> methodsForTest = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            List<Annotation> annotations = List.of(method.getAnnotations());
            for (Annotation annotation : annotations) {
                String annotationSimpleName = annotation.annotationType().getSimpleName();
                switch (annotationSimpleName) {
                    case "Before", "After", "Test":
                        if (methodsForTest.containsKey(annotationSimpleName)) {
                            methodsForTest.get(annotationSimpleName).add(method);
                        } else {
                            List<Method> methods = new ArrayList<>();
                            methods.add(method);
                            methodsForTest.put(annotationSimpleName, methods);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return methodsForTest;
    }

    private void executeTests(Map<String, List<Method>> methods, Class<?> clazz) {

        int total = methods.get("Test").size();
        int passed = 0, failed = 0;

        for (Method method : methods.get("Test")) {
            Object instance;
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                boolean testPrepared = true;

                // @Before section
                for (Method beforeMethod : methods.get("Before")) {
                    if (!isPassed(beforeMethod, instance)) {
                        testPrepared = false;
                        break;
                    }
                }

                // @Test section
                if (testPrepared) {
                    if (isPassed(method, instance)) {
                        passed++;
                    }
                } else {
                    failed++;
                }

                // @after section
                for (Method afterMethod : methods.get("Before")) {
                    isPassed(afterMethod, instance);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("Total tests: %s %nPassed: %s%nFailed: %s %n", total, passed, failed);
    }

    private boolean isPassed(Method method, Object instance) {
        try {
            method.setAccessible(true);
            method.invoke(instance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
