package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.github.vbauer.caesar.runner.AsyncMethodRunner;
import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;

/**
 * Proxy creator that makes asynchronous variant of bean.
 *
 * @author Vladislav Bauer
 */

public final class AsyncProxyCreator {

    private AsyncProxyCreator() {
        throw new UnsupportedOperationException();
    }


    public static <SYNC, ASYNC> ASYNC create(
        final SYNC bean, final Class<ASYNC> asyncInterface, final ExecutorService executor
    ) {
        return create(bean, asyncInterface, executor, true);
    }

    @SuppressWarnings("unchecked")
    public static <SYNC, ASYNC> ASYNC create(
        final SYNC bean, final Class<ASYNC> asyncInterface, final ExecutorService executor, final boolean validate
    ) {
        final AsyncInvocationHandler handler = AsyncInvocationHandler.create(bean, executor);
        final Class<?> beanClass = bean.getClass();
        final ClassLoader classLoader = beanClass.getClassLoader();
        final Class<?>[] interfaces = {
            asyncInterface,
        };

        final ASYNC proxy = (ASYNC) Proxy.newProxyInstance(classLoader, interfaces, handler);
        return validate ? validate(proxy, handler) : proxy;
    }


    private static <T> T validate(final T proxy, final AsyncInvocationHandler handler) {
        final Class<?> targetClass = ReflectionUtils.getClassWithoutProxies(proxy);
        final Method[] methods = targetClass.getDeclaredMethods();

        for (final Method method : methods) {
            if (!ReflectionUtils.isMethodFromObject(method)) {
                final AsyncMethodRunner runner = handler.findAsyncMethodRunner(method);
                if (runner == null) {
                    throw new MissedSyncMethodException(method);
                }
            }
        }

        return proxy;
    }

}
