package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.basic.Consumer;
import com.github.vbauer.caesar.callback.FutureCallbackAdapter;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.google.common.util.concurrent.FutureCallback;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Vladislav Bauer
 */

public class FutureCallbackMethodRunnerTest extends BasicRunnerTest {

    @Test
    public void testWithoutResult() throws Throwable {
        check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().empty(callback);
            }
        }, notNullResultCallback());
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().emptyHello(callback, PARAMETER);
            }
        }, notNullResultCallback());
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        check(new Consumer<FutureCallback<String>>() {
            public void set(final FutureCallback<String> callback) {
                getFutureCallbackAsync().hello(callback, PARAMETER);
            }
        }, new FutureCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER), result);
            }
        });
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        check(new Consumer<FutureCallback<String>>() {
            public void set(final FutureCallback<String> callback) {
                getFutureCallbackAsync().hello(callback, PARAMETER, PARAMETER);
            }
        }, new FutureCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER, PARAMETER), result);
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testException() throws Throwable {
        check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().exception(callback);
            }
        }, new FutureCallbackAdapter<Void>());
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() throws Throwable {
        getFutureCallbackAsync().methodWithoutSyncImpl(new FutureCallbackAdapter<Boolean>());
        Assert.fail();
    }


    private <T> void check(
        final Consumer<FutureCallback<T>> operation, final FutureCallback<T> callback
    ) throws Throwable {
        final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
        final Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();

        final FutureCallback<T> semaphoreCallback = new FutureCallback<T>() {
            public void onSuccess(final T result) {
                try {
                    callback.onSuccess(result);
                } finally {
                    semaphore.release();
                }
            }

            public void onFailure(final Throwable caught) {
                try {
                    error.set(caught);
                    callback.onFailure(caught);
                } finally {
                    semaphore.release();
                }
            }
        };

        operation.set(semaphoreCallback);

        semaphore.acquire();
        final Throwable throwable = error.get();
        if (throwable != null) {
            throw throwable;
        }
    }

    private FutureCallbackAdapter<Void> notNullResultCallback() {
        return new FutureCallbackAdapter<Void>() {
            @Override
            public void onSuccess(final Void result) {
                Assert.assertNull(result);
            }
        };
    }

}
