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
    public void onSuccess(final T result) {
    }

    /**
     * {@inheritDoc}
     */
    public void onFailure(final Throwable t) {
    }

}
