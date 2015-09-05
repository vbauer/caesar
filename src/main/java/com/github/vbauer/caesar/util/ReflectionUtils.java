package com.github.vbauer.caesar.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("unchecked")
public final class ReflectionUtils {

    public static final String PACKAGE_SEPARATOR = ".";
    

    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }


    public static <T> Class<T> getClassWithoutProxies(final Object object) {
        try {
            // XXX: Use HibernateProxyHelper to un-proxy object and get the original class.
            final Class<?> clazz = Class.forName("org.hibernate.proxy.HibernateProxyHelper");
            final Method method = clazz.getDeclaredMethod("getClassWithoutInitializingProxy", Object.class);

            return (Class<T>) method.invoke(null, object);
        } catch (final Exception ex) {
            return getClassSafe(object);
        }
    }

    public static <T> Class<T> getClassSafe(final Object object) {
        return object != null ? (Class<T>) object.getClass() : null;
    }

    public static <T> T createObject(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            return (T) clazz.newInstance();
        } catch (final Throwable ex) {
            return null;
        }
    }

    public static <T> Collection<T> createObjects(final Collection<String> classNames) {
        final List<T> objects = new ArrayList<>();
        for (final String className : classNames) {
            final T object = createObject(className);
            if (object != null) {
                objects.add(object);
            }
        }
        return objects;
    }
    
    public static Collection<String> classNames(final String packageName, final Collection<String> classNames) {
        final List<String> result = new ArrayList<>();
        for (final String className : classNames) {
            result.add(packageName + PACKAGE_SEPARATOR + className);
        }
        return result;        
    }

    public static Method findDeclaredMethod(
        final Class<?> objectClass, final String methodName, final Class<?>[] parameterTypes
    ) {
        try {
            return objectClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (final Throwable ignored) {
            return null;
        }
    }

    public static boolean isMethodFromObject(final Method method) {
        final Method[] methods = Object.class.getDeclaredMethods();
        for (final Method m : methods) {
            if (isSameMethods(method, m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameMethods(final Method m1, final Method m2) {
        final Class<?> returnType1 = m1.getReturnType();
        final Class<?>[] parameterTypes1 = m1.getParameterTypes();
        final String name1 = m1.getName();

        final Class<?> returnType2 = m2.getReturnType();
        final Class<?>[] parameterTypes2 = m2.getParameterTypes();
        final String name2 = m2.getName();

        return name1.equals(name2)
           && returnType1 == returnType2
           && Arrays.equals(parameterTypes1, parameterTypes2);
    }

}
