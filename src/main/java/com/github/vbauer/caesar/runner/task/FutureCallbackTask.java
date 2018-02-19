package com.github.vbauer.caesar.runner.task;

import com.google.common.util.concurrent.FutureCallback;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 * @param <T> result type
 */

public class FutureCallbackTask<T> implements Callable<T> {

    private final Object origin;
    private final Object[] args;
    private final Method syncMethod;
    private final FutureCallback futureCallback;


    public FutureCallbackTask(
        final Object origin, final Method syncMethod, final Object[] args,
        final FutureCallback futureCallback
    ) {
        this.origin = origin;
        this.args = args;
        this.syncMethod = syncMethod;
        this.futureCallback = futureCallback;
    }


    @SuppressWarnings("unchecked")
    public T call() {
        final Object result;

        try {
            result = syncMethod.invoke(origin, args);
        } catch (final Throwable ex) {
            futureCallback.onFailure(ex.getCause());
            return null;
        }

        futureCallback.onSuccess(result);

        return (T) result;
    }

}
