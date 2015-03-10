package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.callback.AsyncCallbackAdapter;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Vladislav Bauer
 */

public class AsyncCallbackMethodRunnerTest extends BasicRunnerTest {

    @Test
    public void testWithoutResult() throws Throwable {
        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                getCallbackAsync().empty(callback);
            }
        }, notNullResultCallback());
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                getCallbackAsync().emptyHello(callback, PARAMETER);
            }
        }, notNullResultCallback());
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        check(new Consumer<AsyncCallback<String>>() {
            public void set(final AsyncCallback<String> callback) {
                getCallbackAsync().hello(callback, PARAMETER);
            }
        }, new AsyncCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER), result);
            }
        });
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        check(new Consumer<AsyncCallback<String>>() {
            public void set(final AsyncCallback<String> callback) {
                getCallbackAsync().hello(callback, PARAMETER, PARAMETER);
            }
        }, new AsyncCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(getSync().hello(PARAMETER, PARAMETER), result);
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testException() throws Throwable {
        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                getCallbackAsync().exception(callback);
            }
        }, new AsyncCallbackAdapter<Void>());
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() throws Throwable {
        getCallbackAsync().methodWithoutSyncImpl(new AsyncCallbackAdapter<Boolean>());
        Assert.fail();
    }


    private <T> void check(
        final Consumer<AsyncCallback<T>> operation, final AsyncCallback<T> callback
    ) throws Throwable {
        final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
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

        operation.set(semaphoreCallback);

        semaphore.acquire();
        final Throwable throwable = error.get();
        if (throwable != null) {
            throw throwable;
        }
    }

    private AsyncCallbackAdapter<Void> notNullResultCallback() {
        return new AsyncCallbackAdapter<Void>() {
            @Override
            public void onSuccess(final Void result) {
                Assert.assertNull(result);
            }
        };
    }


    /**
     * @author Vladislav Bauer
     * @param <T> class type
     */
    private interface Consumer<T> {

        void set(T input);

    }

}
