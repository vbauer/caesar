package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.impl.base.AbstractCallbackMethodRunner;
import com.github.vbauer.caesar.runner.task.FutureCallbackTask;
import com.google.common.util.concurrent.FutureCallback;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class FutureCallbackMethodRunner extends AbstractCallbackMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    protected  <T> Callable<T> createCall(
        final Object origin, final Method syncMethod, final Object callback, final Object[] args
    ) {
        return new FutureCallbackTask<T>(origin, syncMethod, args, (FutureCallback) callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> getCallbackClass() {
        return FutureCallback.class;
    }

}
