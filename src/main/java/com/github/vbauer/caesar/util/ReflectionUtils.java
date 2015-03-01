package com.github.vbauer.caesar.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }


    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassWithoutProxies(final Object object) {
        try {
            // XXX: Use HibernateProxyHelper to un-proxy object and get the original class.
            final Class<?> clazz = Class.forName("org.hibernate.proxy.HibernateProxyHelper");
            final Method method = clazz.getDeclaredMethod("getClassWithoutInitializingProxy", Object.class);

            return (Class<T>) method.invoke(null, object);
        } catch (final Exception ex) {
            try {
                return (Class<T>) object.getClass();
            } catch (final Exception e) {
                return null;
            }
        }
    }

    public static <T> T handleReflectionException(final Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new RuntimeException(ex);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createObject(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            return (T) clazz.newInstance();
        } catch (final Throwable ex) {
            return null;
        }
    }

    public static <T> List<T> createObjects(final String... classNames) {
        final List<T> objects = new ArrayList<T>();
        for (final String className : classNames) {
            final T object = createObject(className);
            if (object != null) {
                objects.add(object);
            }
        }
        return objects;
    }

}
