package com.github.vbauer.caesar.runner.impl.base;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * {@link AbstractAsyncMethodRunner}
 *
 * @author Vladislav Bauer
 */

public abstract class AbstractReturnMethodRunner extends AbstractAsyncMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        final Class<?> returnClass = getReturnClass(returnType);
        return Objects.equals(returnClass, returnType)
            ? ReflectionUtils.findDeclaredMethod(targetClass, methodName, parameterTypes) : null;
    }


    protected abstract Class<?> getReturnClass(Class<?> originReturnType);

}
