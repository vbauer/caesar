package com.github.vbauer.caesar.basic;

import com.github.vbauer.caesar.bean.CallbackAsync;
import com.github.vbauer.caesar.bean.FutureAsync;
import com.github.vbauer.caesar.bean.FutureCallbackAsync;
import com.github.vbauer.caesar.bean.ListenableFutureAsync;
import com.github.vbauer.caesar.bean.ObservableAsync;
import com.github.vbauer.caesar.bean.Sync;
import com.github.vbauer.caesar.proxy.AsyncProxyCreator;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vladislav Bauer
 */

public abstract class BasicRunnerTest extends BasicTest {

    protected static final String PARAMETER = "World";

    private final ThreadLocal<ExecutorService> executorServiceHolder = new ThreadLocal<>();
    private final ThreadLocal<Sync> syncBeanHolder = new ThreadLocal<>();
    private final ThreadLocal<CallbackAsync> callbackAsyncHolder = new ThreadLocal<>();
    private final ThreadLocal<FutureCallbackAsync> futureCallbackAsyncHolder = new ThreadLocal<>();
    private final ThreadLocal<FutureAsync> futureAsyncHolder = new ThreadLocal<>();
    private final ThreadLocal<ListenableFutureAsync> listenableFutureAsyncHolder = new ThreadLocal<>();
    private final ThreadLocal<ObservableAsync> observableAsyncHolder = new ThreadLocal<>();

    @Before
    public final void before() {
        final ExecutorService executor = Executors.newScheduledThreadPool(5);
        final Sync sync = new Sync();
        final CallbackAsync callbackAsync = AsyncProxyCreator.create(sync, CallbackAsync.class, executor, false);
        final FutureCallbackAsync futureCallbackAsync = AsyncProxyCreator.create(sync, FutureCallbackAsync.class, executor, false);
        final FutureAsync futureAsync = AsyncProxyCreator.create(sync, FutureAsync.class, executor, false);
        final ListenableFutureAsync listenableFutureAsync = AsyncProxyCreator.create(sync, ListenableFutureAsync.class, executor, false);
        final ObservableAsync observableAsync = AsyncProxyCreator.create(sync, ObservableAsync.class, executor, false);

        executorServiceHolder.set(executor);
        syncBeanHolder.set(sync);
        callbackAsyncHolder.set(callbackAsync);
        futureCallbackAsyncHolder.set(futureCallbackAsync);
        futureAsyncHolder.set(futureAsync);
        listenableFutureAsyncHolder.set(listenableFutureAsync);
        observableAsyncHolder.set(observableAsync);
    }

    @After
    public final void after() {
        getExecutorService().shutdown();
        executorServiceHolder.remove();
        syncBeanHolder.remove();
        callbackAsyncHolder.remove();
        futureCallbackAsyncHolder.remove();
        futureAsyncHolder.remove();
        listenableFutureAsyncHolder.remove();
        observableAsyncHolder.remove();
    }


    protected ExecutorService getExecutorService() {
        return executorServiceHolder.get();
    }

    protected Sync getSync() {
        return syncBeanHolder.get();
    }

    protected CallbackAsync getCallbackAsync() {
        return callbackAsyncHolder.get();
    }

    protected FutureCallbackAsync getFutureCallbackAsync() {
        return futureCallbackAsyncHolder.get();
    }

    protected FutureAsync getFutureAsync() {
        return futureAsyncHolder.get();
    }

    protected ListenableFutureAsync getListenableFutureAsync() {
        return listenableFutureAsyncHolder.get();
    }

    protected ObservableAsync getObservableAsync() {
        return observableAsyncHolder.get();
    }

}
