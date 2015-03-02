package com.github.vbauer.caesar.bean;

import java.util.concurrent.Future;

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

}
