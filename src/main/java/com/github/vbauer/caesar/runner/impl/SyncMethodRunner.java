package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.impl.base.AbstractReturnMethodRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class SyncMethodRunner extends AbstractReturnMethodRunner {

    @Override
    protected Class<?> getReturnClass(final Class<?> originReturnType) {
        return originReturnType;
    }

    @Override
    public Object processResultFuture(
        final Future<?> future, final ExecutorService executor
    ) throws Throwable {
        return future.get();
    }

}
