package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;
import rx.Observable;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer 
 */

@SuppressWarnings("all")
public class ObservableMethodRunner extends AbstractAsyncMethodRunner {

    @Override
    public Object wrapResultFuture(final Future<?> future) {
        return Observable.from(future);
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
