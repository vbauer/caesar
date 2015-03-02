package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;
import rx.Observable;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer 
 */

public class ObservableMethodRunner extends AbstractAsyncMethodRunner {

    public <T> Callable<T> createCall(
        final Object origin, final Method syncMethod, final Object[] args
    ) {
        // TODO
        return null;
    }

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        return (Observable.class == returnType)
            ? ReflectionUtils.findDeclaredMethod(targetClass, methodName, parameterTypes) : null;
    }
    
}
