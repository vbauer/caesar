package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.callback.AsyncCallback;

/**
 * @author Vladislav Bauer
 */

public interface CallbackAsync {

    void hello(String name1, String name2, AsyncCallback<String> callback);

    void hello(String name, AsyncCallback<String> callback);

    void emptyHello(String name, AsyncCallback<Void> callback);

    void empty(AsyncCallback<Void> callback);

    void exception(AsyncCallback<Void> callback);

    void methodWithoutSyncImpl(AsyncCallback<Boolean> callback);

}
