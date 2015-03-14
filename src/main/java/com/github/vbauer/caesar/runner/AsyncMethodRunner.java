package com.github.vbauer.caesar.runner;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public interface AsyncMethodRunner {

    Method findSyncMethod(Object origin, Method asyncMethod);

    <T> Callable<T> createCall(Object origin, Method syncMethod, Object[] args);

    Object wrapResultFuture(Future<?> future, ExecutorService executor);

}
