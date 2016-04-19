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
    public void testTimeout() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<Boolean>>() {
            @Override
            public void set(final FutureCallback<Boolean> callback) {
                getFutureCallbackAsync().timeout(callback);
            }
        }, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(final Boolean result) {
                Assert.fail();
            }
            @Override
            public void onFailure(final Throwable t) {
                Assert.assertNotNull(t);
            }
        }, false));
    }

    @Test
    public void testWithoutResult() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().empty(callback);
            }
        }, notNullResultCallback(), true));
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().emptyHello(callback, PARAMETER);
            }
        }, notNullResultCallback(), true));
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<String>>() {
            public void set(final FutureCallback<String> callback) {
                getFutureCallbackAsync().hello(callback, PARAMETER);
            }
        }, new FutureCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER), result);
            }
        }, true));
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<String>>() {
            public void set(final FutureCallback<String> callback) {
                getFutureCallbackAsync().hello(callback, PARAMETER, PARAMETER);
            }
        }, new FutureCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER, PARAMETER), result);
            }
        }, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testException() throws Throwable {
        Assert.assertTrue(check(new Consumer<FutureCallback<Void>>() {
            public void set(final FutureCallback<Void> callback) {
                getFutureCallbackAsync().exception(callback);
            }
        }, new FutureCallbackAdapter<Void>(), true));
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() throws Throwable {
        getFutureCallbackAsync().methodWithoutSyncImpl(new FutureCallbackAdapter<Boolean>());
        Assert.fail();
    }


    private <T> boolean check(
        final Consumer<FutureCallback<T>> operation, final FutureCallback<T> callback,
        final boolean failOnError
    ) throws Throwable {
        final AtomicReference<Throwable> error = new AtomicReference<>();
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

        if (failOnError) {
            final Throwable throwable = error.get();
            if (throwable != null) {
                throw throwable;
            }
        }

        return true;
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
