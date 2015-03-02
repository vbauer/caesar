package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class FutureMethodRunner extends AbstractAsyncMethodRunner {

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        return (Future.class == returnType)
            ? ReflectionUtils.findDeclaredMethod(targetClass, methodName, parameterTypes) : null;
    }

}
