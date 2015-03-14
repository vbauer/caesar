package com.github.vbauer.caesar.bean;

import com.google.common.util.concurrent.FutureCallback;

/**
 * @author Vladislav Bauer
 */

public interface FutureCallbackAsync {

    void hello(FutureCallback<String> callback, String name1, String name2);

    void hello(FutureCallback<String> callback, String name);

    void emptyHello(FutureCallback<Void> callback, String name);

    void empty(FutureCallback<Void> callback);

    void exception(FutureCallback<Void> callback);

    void methodWithoutSyncImpl(FutureCallback<Boolean> callback);

}
