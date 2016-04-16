package com.github.vbauer.caesar.callback;

/**
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public interface AsyncCallback<T> {

    /**
     * Callback-method on success.
     *
     * @param result result of operation
     */
    void onSuccess(T result);

    /**
     * Callback-method on failure.
     *
     * @param caught exception
     */
    void onFailure(Throwable caught);

}
