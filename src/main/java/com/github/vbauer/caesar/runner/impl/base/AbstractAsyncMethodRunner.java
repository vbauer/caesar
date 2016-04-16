package com.github.vbauer.caesar.runner.impl.base;

import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.runner.task.SimpleInvokeTask;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public abstract class AbstractAsyncMethodRunner implements AsyncMethodRunner {

    /**
     * {@inheritDoc}
     */
    public final Method findSyncMethod(final Object origin, final Method asyncMethod) {
        final Class<?> targetClass = ReflectionUtils.getClassWithoutProxies(origin);
        final String methodName = asyncMethod.getName();

        final Class<?> returnType = asyncMethod.getReturnType();
        final Class<?>[] parameterTypes = asyncMethod.getParameterTypes();

        return findSyncMethod(targetClass, methodName, returnType, parameterTypes);
    }

    /**
     * {@inheritDoc}
     */
    public <T> Callable<T> createCall(
        final Object origin, final Method syncMethod, final Object[] args
    ) {
        return new SimpleInvokeTask<>(origin, syncMethod, args);
    }

    /**
     * {@inheritDoc}
     */
    public Object processResultFuture(
        final Future<?> future, final ExecutorService executor
    ) throws Throwable {
        return future;
    }


    /**
     * {@inheritDoc}
     */
    protected abstract Method findSyncMethod(
        Class<?> targetClass, String methodName, Class<?> returnType, Class<?>[] parameterTypes
    );

}
