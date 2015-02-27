package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.callback.AsyncCallback;

import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public interface Async {

    Future<String> hello(String name1, String name2);

    void hello(String name1, String name2, AsyncCallback<String> callback);

    Future<String> hello(String name);

    void hello(String name, AsyncCallback<String> callback);

    Future<Void> emptyHello(String name);

    void emptyHello(String name, AsyncCallback<Void> callback);

    Future<Void> empty();

    void empty(AsyncCallback<Void> callback);

    Future<Void> exception();

    void exception(AsyncCallback<Void> callback);

    Future<Boolean> methodWithoutSyncImpl();

    void methodWithoutSyncImpl(AsyncCallback<Boolean> callback);

}
