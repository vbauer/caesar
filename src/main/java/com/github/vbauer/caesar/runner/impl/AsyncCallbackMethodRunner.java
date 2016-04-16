package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.runner.impl.base.AbstractCallbackMethodRunner;
import com.github.vbauer.caesar.runner.task.AsyncCallbackTask;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class AsyncCallbackMethodRunner extends AbstractCallbackMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    protected  <T> Callable<T> createCall(
        final Object origin, final Method syncMethod, final Object callback, final Object[] args
    ) {
        return new AsyncCallbackTask<T>(origin, syncMethod, args, (AsyncCallback) callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> getCallbackClass() {
        return AsyncCallback.class;
    }

}
