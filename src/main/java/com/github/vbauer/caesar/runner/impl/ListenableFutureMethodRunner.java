package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.impl.base.AbstractReturnMethodRunner;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class ListenableFutureMethodRunner extends AbstractReturnMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object processResultFuture(final Future<?> future, final ExecutorService executor) {
        return JdkFutureAdapters.listenInPoolThread(future, executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> getReturnClass(final Class<?> originReturnType) {
        return ListenableFuture.class;
    }

}
