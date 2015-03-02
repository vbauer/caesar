package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 */

public class AsyncCallbackMethodRunner extends AbstractAsyncMethodRunner {

    @SuppressWarnings("rawtypes")
    public <T> Callable<T> createCall(final Object origin, final Method syncMethod, final Object[] args) {
        final int lastIndex = args.length - 1;
        final Object lastParam = args[lastIndex];
        final Object[] lessArgs = Arrays.copyOf(args, lastIndex);
        final AsyncCallback asyncCallback = (AsyncCallback) lastParam;

        return new CallableTask<T>(origin, syncMethod, lessArgs, asyncCallback);
    }

    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName, final Class<?> returnType, final Class<?>[] parameterTypes
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


    /**
     * @author Vladislav Bauer
     * @param <T> result type
     */

    @SuppressWarnings("rawtypes")
    private static final class CallableTask<T> implements Callable<T> {

        private final Object origin;
        private final Object[] args;
        private final Method syncMethod;
        private final AsyncCallback asyncCallback;


        private CallableTask(
            final Object origin, final Method syncMethod, final Object[] args, final AsyncCallback asyncCallback
        ) {
            this.origin = origin;
            this.args = args;
            this.syncMethod = syncMethod;
            this.asyncCallback = asyncCallback;
        }


        @SuppressWarnings("unchecked")
        public T call() {
            Object result;

            try {
                result = syncMethod.invoke(origin, args);
            } catch (final Throwable ex) {
                try {
                    asyncCallback.onFailure(ex.getCause());
                } catch (final Throwable e) {
                    return ReflectionUtils.handleReflectionException(e);
                }
                return null;
            }

            try {
                asyncCallback.onSuccess(result);
            } catch (final Throwable ex) {
                ReflectionUtils.handleReflectionException(ex);
            }

            return (T) result;
        }
    }

}
