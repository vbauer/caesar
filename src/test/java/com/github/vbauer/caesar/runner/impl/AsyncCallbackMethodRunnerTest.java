package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.callback.AsyncCallbackAdapter;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author Vladislav Bauer
 */

public class AsyncCallbackMethodRunnerTest extends BasicRunnerTest {

    @Test
    public void testTimeout() throws Throwable {
        check(
            callback -> getCallbackAsync().timeout(callback),
            new AsyncCallback<Boolean>() {
                @Override
                public void onSuccess(final Boolean result) {
                    Assert.fail();
                }
                @Override
                public void onFailure(final Throwable caught) {
                    Assert.assertNull(caught);
                }
            },
            false
        );
    }

    @Test
    public void testWithoutResult() throws Throwable {
        Assert.assertTrue(
            check(
                callback -> getCallbackAsync().empty(callback),
                notNullResultCallback(),
                true
            )
        );
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        Assert.assertTrue(
            check(
                callback -> getCallbackAsync().emptyHello(callback, PARAMETER),
                notNullResultCallback(),
                true
            )
        );
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertTrue(
            check(
                callback -> getCallbackAsync().hello(callback, PARAMETER),
                new AsyncCallbackAdapter<String>() {
                    @Override
                    public void onSuccess(final String result) {
                        Assert.assertEquals(getSync().hello(PARAMETER), result);
                    }
                },
                true
            )
        );
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertTrue(
            check(
                callback -> getCallbackAsync().hello(callback, PARAMETER, PARAMETER),
                new AsyncCallbackAdapter<String>() {
                    @Override
                    public void onSuccess(final String result) {
                        Assert.assertEquals(getSync().hello(PARAMETER, PARAMETER), result);
                    }
                },
                true
            )
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testException() throws Throwable {
        Assert.assertTrue(
            check(
                callback -> getCallbackAsync().exception(callback),
                new AsyncCallbackAdapter<Void>(),
                true
            )
        );
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() {
        getCallbackAsync().methodWithoutSyncImpl(new AsyncCallbackAdapter<>());
        Assert.fail();
    }


    private <T> boolean check(
        final Consumer<AsyncCallback<T>> operation, final AsyncCallback<T> callback,
        final boolean failOnError
    ) throws Throwable {
        final AtomicReference<Throwable> error = new AtomicReference<>();
        final Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();

        final AsyncCallback<T> semaphoreCallback = new AsyncCallback<T>() {
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

        operation.accept(semaphoreCallback);
        semaphore.acquire();

        if (failOnError) {
            final Throwable throwable = error.get();
            if (throwable != null) {
                throw throwable;
            }
        }

        return true;
    }

    private AsyncCallbackAdapter<Void> notNullResultCallback() {
        return new AsyncCallbackAdapter<Void>() {
            @Override
            public void onSuccess(final Void result) {
                Assert.assertNull(result);
            }
        };
    }

}
