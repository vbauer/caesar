package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @author Vladislav Bauer
 */

public final class AsyncInvocationHandler implements InvocationHandler {

    private static final String[] METHOD_RUNNERS = {
        "com.github.vbauer.caesar.runner.impl.AsyncCallbackMethodRunner",
        "com.github.vbauer.caesar.runner.impl.FutureMethodRunner",
    };


    private final Object origin;
    private final ExecutorService executor;
    private final List<AsyncMethodRunner> runners;


    private AsyncInvocationHandler(
        final Object origin, final ExecutorService executor, final List<AsyncMethodRunner> runners
    ) {
        this.origin = origin;
        this.executor = executor;
        this.runners = runners;
    }


    public static AsyncInvocationHandler create(final Object origin, final ExecutorService executor) {
        final List<AsyncMethodRunner> runners = ReflectionUtils.createObjects(METHOD_RUNNERS);
        return new AsyncInvocationHandler(origin, executor, runners);
    }


    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        final AsyncMethodRunner runner = findAsyncMethodRunner(method);
        if (runner != null) {
            return runAsyncMethod(runner, method, args);
        }
        throw new MissedSyncMethodException(method, args);
    }


    /*package*/ AsyncMethodRunner findAsyncMethodRunner(final Method method) {
        for (final AsyncMethodRunner runner : runners) {
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
