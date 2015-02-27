package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Vladislav Bauer
 */

public abstract class AbstractAsyncMethodRunner implements AsyncMethodRunner {

    public final Method findSyncMethod(final Object origin, final Method asyncMethod) {
        final Class<?> targetClass = ReflectionUtils.getClassWithoutProxies(origin);
        final String methodName = asyncMethod.getName();

        final Class<?> returnType = asyncMethod.getReturnType();
        final Class<?>[] parameterTypes = asyncMethod.getParameterTypes();

        return findSyncMethod(targetClass, methodName, returnType, parameterTypes);
    }


    protected abstract Method findSyncMethod(
        Class<?> targetClass, String methodName, Class<?> returnType, Class<?>[] parameterTypes
    );

}
