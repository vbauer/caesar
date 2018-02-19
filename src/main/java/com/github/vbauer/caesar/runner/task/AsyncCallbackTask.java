package com.github.vbauer.caesar.runner.task;

import com.github.vbauer.caesar.callback.AsyncCallback;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 * @param <T> result type
 */

public class AsyncCallbackTask<T> implements Callable<T> {

    private final Object origin;
    private final Object[] args;
    private final Method syncMethod;
    private final AsyncCallback asyncCallback;


    public AsyncCallbackTask(
        final Object origin, final Method syncMethod, final Object[] args, final AsyncCallback asyncCallback
    ) {
        this.origin = origin;
        this.args = args;
        this.syncMethod = syncMethod;
        this.asyncCallback = asyncCallback;
    }


    @SuppressWarnings("unchecked")
    public T call() {
        final Object result;

        try {
            result = syncMethod.invoke(origin, args);
        } catch (final Throwable ex) {
            asyncCallback.onFailure(ex.getCause());
            return null;
        }

        asyncCallback.onSuccess(result);

        return (T) result;
    }

}
