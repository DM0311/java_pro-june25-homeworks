package ru.otus.java.pro.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public record Signature(String name, String[] params) {
    static Signature toSignature(Method m) {

        return new Signature(
                m.getName(),
                Arrays.stream(m.getParameterTypes()).map(Class::toString).toArray(String[]::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Signature signature = (Signature) o;
        return Objects.equals(name, signature.name) && Arrays.equals(params, signature.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
