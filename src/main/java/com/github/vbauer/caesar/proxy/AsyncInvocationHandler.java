package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.runner.AsyncMethodRunnerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @author Vladislav Bauer
 */

public final class AsyncInvocationHandler implements InvocationHandler {

    private static final Collection<AsyncMethodRunner> METHOD_RUNNERS =
        AsyncMethodRunnerFactory.createMethodRunners();
    
    private final Object origin;
    private final ExecutorService executor;


    private AsyncInvocationHandler(final Object origin, final ExecutorService executor) {
        this.origin = origin;
        this.executor = executor;
    }


    public static AsyncInvocationHandler create(final Object origin, final ExecutorService executor) {
        return new AsyncInvocationHandler(origin, executor);
    }


    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        final AsyncMethodRunner runner = findAsyncMethodRunner(method);
        if (runner != null) {
            return runAsyncMethod(runner, method, args);
        }
        throw new MissedSyncMethodException(method, args);
    }


    /*package*/ AsyncMethodRunner findAsyncMethodRunner(final Method method) {
        for (final AsyncMethodRunner runner : METHOD_RUNNERS) {
            final Method syncMethod = runner.findSyncMethod(origin, method);
            if (syncMethod != null) {
                return runner;
            }
        }
        return null;
    }


    private Object runAsyncMethod(final AsyncMethodRunner runner, final Method method, final Object[] args) {
        final Method syncMethod = runner.findSyncMethod(origin, method);
        final boolean methodAccessible = syncMethod.isAccessible();
        syncMethod.setAccessible(methodAccessible);

        try {
            final Callable<Object> task = runner.createCall(origin, syncMethod, args);
            return executor.submit(task);
        } finally {
            syncMethod.setAccessible(methodAccessible);
        }
    }

}
