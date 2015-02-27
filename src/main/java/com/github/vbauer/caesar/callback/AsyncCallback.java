package com.github.vbauer.caesar.callback;

/**
 * @param <T> type of result
 * @author Vladislav Bauer
 */

public interface AsyncCallback<T> {

    void onSuccess(T result);

    void onFailure(Throwable caught);

}
