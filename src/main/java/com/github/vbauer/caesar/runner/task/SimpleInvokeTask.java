package com.github.vbauer.caesar.runner.task;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 * @param <T> result type
 */

public class SimpleInvokeTask<T> implements Callable<T> {

    private final Method syncMethod;
    private final Object[] args;
    private final Object origin;


    public SimpleInvokeTask(final Object origin, final Method syncMethod, final Object[] args) {
        this.syncMethod = syncMethod;
        this.args = args;
        this.origin = origin;
    }


    @SuppressWarnings("unchecked")
    public T call() throws Exception {
        return (T) syncMethod.invoke(origin, args);
    }

}
