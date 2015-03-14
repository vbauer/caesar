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

    @Override
    public Object wrapResultFuture(final Future<?> future, final ExecutorService executor) {
        return JdkFutureAdapters.listenInPoolThread(future, executor);
    }

    @Override
    protected Class<?> getReturnClass() {
        return ListenableFuture.class;
    }

}
