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
        final AsyncCallback asyncCallback = (AsyncCallback) args[0];
        final Object[] restArgs = Arrays.copyOfRange(args, 1, args.length);

        return new AsyncCallbackTask<T>(origin, syncMethod, restArgs, asyncCallback);
    }

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        if (void.class == returnType && parameterTypes != null && parameterTypes.length > 0) {
            final Class<?> lastParam = (Class<?>) parameterTypes[0];

            if (AsyncCallback.class.isAssignableFrom(lastParam)) {
                final Class<?>[] restParamTypes = Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);
                return ReflectionUtils.findDeclaredMethod(targetClass, methodName, restParamTypes);
            }

        }
        return null;
    }

}
