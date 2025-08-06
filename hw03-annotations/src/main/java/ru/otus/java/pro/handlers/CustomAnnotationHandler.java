package ru.otus.java.pro.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAnnotationHandler {

    private static Map<String, List<String>> methodsForTest = new HashMap<>();

    public static void runTests(Class<?> clazz) {

        int total = 0, passed = 0, failed = 0;

        for (Method method : clazz.getDeclaredMethods()) {
            List<Annotation> annotations = List.of(method.getAnnotations());
            for (Annotation annotation : annotations) {
                String annotationSimpleName = annotation.annotationType().getSimpleName();
                switch (annotationSimpleName) {
                    case "Before", "After", "Test":
                        total++;
                        if (methodsForTest.containsKey(annotationSimpleName)) {
                            methodsForTest.get(annotationSimpleName).add(method.getName());
                        } else {
                            List<String> methods = new ArrayList<>();
                            methods.add(method.getName());
                            methodsForTest.put(annotationSimpleName, methods);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        // @Before run
        for (String methodName : methodsForTest.get("Before")) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                var method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(instance);
                passed++;
            } catch (Exception e) {
                failed++;
            }
        }
        // @Test run
        for (String methodName : methodsForTest.get("Test")) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                var method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(instance);
                passed++;
            } catch (Exception e) {
                failed++;
            }
        }
        // @After run
        for (String methodName : methodsForTest.get("After")) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                var method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(instance);
                passed++;
            } catch (Exception e) {
                failed++;
            }
        }
        System.out.printf("Total tests: %s %nPassed: %s%nFailed: %s %n", total, passed, failed);
        System.out.println(methodsForTest);

        methodsForTest.clear();
    }
}
