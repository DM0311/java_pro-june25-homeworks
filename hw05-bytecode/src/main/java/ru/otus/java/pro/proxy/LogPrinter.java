package ru.otus.java.pro.proxy;

import static java.util.Arrays.stream;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.annotation.Log;
import ru.otus.java.pro.source.Calculation;
import ru.otus.java.pro.source.CalculationImpl;

public class LogPrinter {
    private static final Logger logger = LoggerFactory.getLogger(LogPrinter.class);

    public LogPrinter() {}

    public static Calculation createCalculation() {
        InvocationHandler handler = new CalculationInvocationHandler(new CalculationImpl());
        return (Calculation)
                Proxy.newProxyInstance(LogPrinter.class.getClassLoader(), new Class<?>[] {Calculation.class}, handler);
    }

    static class CalculationInvocationHandler implements InvocationHandler {
        private final Calculation clazz;
        private final Set<Signature> methods;

        public CalculationInvocationHandler(Calculation clazz) {
            this.clazz = clazz;
            this.methods = stream(clazz.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(Signature::toSignature)
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methods.contains(Signature.toSignature(method))) {
                logger.info("executed method: {}, param: {}", method, Arrays.toString(args));
            }
            return method.invoke(clazz, args);
        }
    }
}
