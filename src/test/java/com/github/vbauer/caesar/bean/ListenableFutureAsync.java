package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.annotation.Timeout;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author Vladislav Bauer
 */

public interface ListenableFutureAsync {

    ListenableFuture<String> hello(String name1, String name2);

    ListenableFuture<String> hello(String name);

    ListenableFuture<Void> emptyHello(String name);

    ListenableFuture<Void> empty();

    ListenableFuture<Void> exception();

    ListenableFuture<Boolean> methodWithoutSyncImpl();

    @Timeout(value = 1, unit = TimeUnit.MILLISECONDS)
    ListenableFuture<Boolean> timeout();

}
