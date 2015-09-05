package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.annotation.Timeout;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.runner.AsyncMethodRunnerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.*;

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


    public Object invoke(
        final Object proxy, final Method method, final Object[] args
    ) throws Throwable {
        final AsyncMethodRunner runner = findAsyncMethodRunner(method);
        if (runner == null) {
            throw new MissedSyncMethodException(method, args);
        }
        return runAsyncMethod(runner, method, args);
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


    private Object runAsyncMethod(
        final AsyncMethodRunner runner, final Method method, final Object[] args
    ) throws Throwable {
        final Method syncMethod = runner.findSyncMethod(origin, method);
        if (syncMethod == null) {
            throw new MissedSyncMethodException(method, args);
        }

        final boolean methodAccessible = syncMethod.isAccessible();
        syncMethod.setAccessible(methodAccessible);

        try {
            final Callable<Object> task = runner.createCall(origin, syncMethod, args);
            final Timeout timeout = getTimeout(method);
            final Future<Object> future = schedule(task, timeout);
            return runner.processResultFuture(future, executor);
        } finally {
            syncMethod.setAccessible(methodAccessible);
        }
    }

    private Timeout getTimeout(final Method asyncMethod) {
        final Timeout annotation = asyncMethod.getAnnotation(Timeout.class);
        if (annotation != null) {
            return annotation;
        }

        final Class<?> originClass = origin.getClass();
        return originClass.getAnnotation(Timeout.class);
    }

    private Future<Object> schedule(final Callable<Object> task, final Timeout timeout) {
        final Future<Object> future = executor.submit(task);

        if (executor instanceof ScheduledExecutorService && timeout != null) {
            final long value = timeout.value();
            if (value > 0) {
                final TimeUnit unit = timeout.unit();
                final Runnable cancelOperation = createCancelOperation(future);
                final ScheduledExecutorService scheduler = (ScheduledExecutorService) executor;
                scheduler.schedule(cancelOperation, value, unit);
            }
        }

        return future;
    }

    private Runnable createCancelOperation(final Future<Object> future) {
        return new Runnable() {
            @Override
            public void run() {
                future.cancel(true);
            }
        };
    }

}
