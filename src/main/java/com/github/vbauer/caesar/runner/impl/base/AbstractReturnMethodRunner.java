package com.github.vbauer.caesar.runner.impl.base;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Vladislav Bauer
 */

public abstract class AbstractReturnMethodRunner extends AbstractAsyncMethodRunner {

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        final Class<?> returnClass = getReturnClass();
        return (returnClass == returnType)
            ? ReflectionUtils.findDeclaredMethod(targetClass, methodName, parameterTypes) : null;
    }


    protected abstract Class<?> getReturnClass();

}
