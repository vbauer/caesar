package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.callback.AsyncCallback;

/**
 * @author Vladislav Bauer
 */

public interface CallbackAsync {

    void hello(AsyncCallback<String> callback, String name1, String name2);

    void hello(AsyncCallback<String> callback, String name);

    void emptyHello(AsyncCallback<Void> callback, String name);

    void empty(AsyncCallback<Void> callback);

    void exception(AsyncCallback<Void> callback);

    void methodWithoutSyncImpl(AsyncCallback<Boolean> callback);

}
