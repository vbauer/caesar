package com.github.vbauer.caesar.callback;

/**
 * {@link AsyncCallback}
 *
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public class AsyncCallbackAdapter<T> implements AsyncCallback<T> {

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
    public void onFailure(final Throwable caught) {
        // Do nothing.
    }

}
