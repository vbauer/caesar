package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.annotation.Timeout;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Vladislav Bauer
 */

public interface FutureAsync {

    Future<String> hello(String name1, String name2);

    Future<String> hello(String name);

    Future<Void> emptyHello(String name);

    Future<Void> empty();

    Future<Void> exception();

    Future<Boolean> methodWithoutSyncImpl();

    @Timeout(value = 1, unit = TimeUnit.MILLISECONDS)
    Future<Boolean> timeout();
}
