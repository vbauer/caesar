package com.github.vbauer.caesar.runner.impl.base;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 */

public abstract class AbstractCallbackMethodRunner extends AbstractAsyncMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Callable<T> createCall(final Object origin, final Method syncMethod, final Object[] args) {
        final Object asyncCallback = args[0];
        final Object[] restArgs = Arrays.copyOfRange(args, 1, args.length);

        return createCall(origin, syncMethod, asyncCallback, restArgs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Method findSyncMethod(
        final Class<?> targetClass, final String methodName,
        final Class<?> returnType, final Class<?>[] parameterTypes
    ) {
        if (void.class == returnType && parameterTypes != null && parameterTypes.length > 0) {
            final Class<?> lastParam = parameterTypes[0];
            final Class<?> callbackClass = getCallbackClass();

            if (callbackClass.isAssignableFrom(lastParam)) {
                final Class<?>[] restParamTypes =
                    Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);

                return ReflectionUtils.findDeclaredMethod(targetClass, methodName, restParamTypes);
            }

        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    protected abstract <T> Callable<T> createCall(
        Object origin, Method syncMethod, Object callback, Object[] args
    );

    /**
     * {@inheritDoc}
     */
    protected abstract Class<?> getCallbackClass();

}
