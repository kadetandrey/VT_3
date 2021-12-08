package by.bsuir.server.app;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T>void register(Class<T> declarationType, T implementationType) {
        services.put(declarationType, implementationType);
    }

    @SuppressWarnings("unchecked")
    public static <T>T locate(Class<T> declarationType) {
        return (T)services.get(declarationType);
    }
}
