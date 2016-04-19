package com.github.vbauer.caesar.callback;

import com.google.common.util.concurrent.FutureCallback;

/**
 * {@link FutureCallback}
 *
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public class FutureCallbackAdapter<T> implements FutureCallback<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(final T result) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure(final Throwable t) {
        // Do nothing.
    }

}
