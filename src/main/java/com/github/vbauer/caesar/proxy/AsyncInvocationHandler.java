package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.runner.impl.AsyncCallbackMethodRunner;
import com.github.vbauer.caesar.runner.impl.FutureMethodRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * @author Vladislav Bauer
 */

public final class AsyncInvocationHandler implements InvocationHandler {

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
        final List<AsyncMethodRunner> runners = Arrays.<AsyncMethodRunner>asList(
                new AsyncCallbackMethodRunner(),
                new FutureMethodRunner()
        );

        return new AsyncInvocationHandler(origin, executor, runners);
    }


    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        final AsyncMethodRunner runner = findAsyncMethodRunner(method);
        if (runner != null) {
            return runAsyncMethod(runner, method, args);
        }
        return throwError(method, args);
    }


    /*package*/ Object throwError(final Method method, final Object[] args) {
        throw new UnsupportedOperationException(String.format(
            "Can't find appropriate sync-method \"%s\", parameters: %s", method, Arrays.toString(args)
        ));
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
