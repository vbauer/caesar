package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.impl.base.AbstractReturnMethodRunner;
import rx.Observable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer 
 */

@SuppressWarnings("all")
public class ObservableMethodRunner extends AbstractReturnMethodRunner {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object processResultFuture(final Future<?> future, final ExecutorService executor) {
        return Observable.from(future);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<?> getReturnClass(final Class<?> originReturnType) {
        return Observable.class;
    }

}
