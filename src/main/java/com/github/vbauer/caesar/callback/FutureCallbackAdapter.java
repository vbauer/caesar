package com.github.vbauer.caesar.callback;

import com.google.common.util.concurrent.FutureCallback;

/**
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public class FutureCallbackAdapter<T> implements FutureCallback<T> {

    public void onSuccess(final T result) {
    }

    public void onFailure(final Throwable t) {
    }

}
