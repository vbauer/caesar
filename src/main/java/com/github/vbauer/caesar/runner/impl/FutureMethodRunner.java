package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.AbstractAsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public class FutureMethodRunner extends AbstractAsyncMethodRunner {

    public <T> Callable<T> createCall(final Object origin, final Method syncMethod, final Object[] args) {
        return new CallableTask<T>(origin, syncMethod, args);
    }


    @Override
    protected Method findSyncMethod(
            final Class<?> targetClass, final String methodName, final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        if (Future.class == returnType) {
            try {
                return targetClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (final Throwable ignored) {
                // Ignored.
            }
        }
        return null;
    }


    /**
     * @author Vladislav Bauer
     * @param <T> result type
     */

    private final class CallableTask<T> implements Callable<T> {

        private final Method syncMethod;
        private final Object[] args;
        private final Object origin;


        private CallableTask(final Object origin, final Method syncMethod, final Object[] args) {
            this.syncMethod = syncMethod;
            this.args = args;
            this.origin = origin;
        }


        @SuppressWarnings("unchecked")
        public T call() throws Exception {
            try {
                return (T) syncMethod.invoke(origin, args);
            } catch (final Throwable ex) {
                return ReflectionUtils.handleReflectionException(ex);
            }
        }

    }

}
