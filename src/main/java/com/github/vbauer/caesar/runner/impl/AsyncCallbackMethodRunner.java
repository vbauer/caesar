package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.runner.task.AsyncCallbackTask;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class AsyncCallbackMethodRunner extends AbstractAsyncMethodRunner {

    @Override
    public <T> Callable<T> createCall(final Object origin, final Method syncMethod, final Object[] args) {
        final int lastIndex = args.length - 1;
        final Object lastParam = args[lastIndex];
        final Object[] lessArgs = Arrays.copyOf(args, lastIndex);
        final AsyncCallback asyncCallback = (AsyncCallback) lastParam;

        return new AsyncCallbackTask<T>(origin, syncMethod, lessArgs, asyncCallback);
    }

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        if (void.class == returnType && parameterTypes != null && parameterTypes.length > 0) {
            final int lastIndex = parameterTypes.length - 1;
            final Class<?> lastParam = parameterTypes[lastIndex];

            if (AsyncCallback.class.isAssignableFrom(lastParam)) {
                final Class<?>[] paramTypes = Arrays.copyOf(parameterTypes, lastIndex);
                return ReflectionUtils.findDeclaredMethod(targetClass, methodName, paramTypes);
            }

        }
        return null;
    }

}
