package com.github.vbauer.caesar.callback;

/**
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public class AsyncCallbackAdapter<T> implements AsyncCallback<T> {

    public void onSuccess(final T result) {
    }

    public void onFailure(final Throwable caught) {
    }

}
