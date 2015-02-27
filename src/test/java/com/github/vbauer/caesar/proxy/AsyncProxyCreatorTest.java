package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.basic.BasicTest;
import com.github.vbauer.caesar.bean.Async;
import com.github.vbauer.caesar.bean.Sync;
import com.github.vbauer.caesar.callback.AsyncCallback;
import com.github.vbauer.caesar.callback.AsyncCallbackAdapter;
import org.junit.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Vladislav Bauer
 */

public class AsyncProxyCreatorTest extends BasicTest {

    private static final String PARAMETER = "World";


    private static ExecutorService executorService;
    private static Sync syncBean;
    private static Async asyncBean;


    @BeforeClass
    public static void setUp() {
        executorService = Executors.newFixedThreadPool(5);
        syncBean = new Sync();
        asyncBean = AsyncProxyCreator.create(syncBean, Async.class, executorService, false);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        executorService.shutdown();
    }


    @Test
    public void testConstructorContract() throws Exception {
        checkUtilConstructorContract(AsyncProxyCreator.class);
    }

    @Test
    public void testWithoutResult() throws Throwable {
        syncBean.empty();
        Assert.assertNotNull(asyncBean.empty());

        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                asyncBean.empty(callback);
            }
        }, new AsyncCallbackAdapter<Void>() {
            @Override
            public void onSuccess(final Void result) {
                Assert.assertNull(result);
            }
        });
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        syncBean.emptyHello(PARAMETER);
        Assert.assertNotNull(asyncBean.emptyHello(PARAMETER));

        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                asyncBean.emptyHello(PARAMETER, callback);
            }
        }, new AsyncCallbackAdapter<Void>() {
            @Override
            public void onSuccess(final Void result) {
                Assert.assertNull(result);
            }
        });
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertEquals(syncBean.hello(PARAMETER), asyncBean.hello(PARAMETER).get());

        check(new Consumer<AsyncCallback<String>>() {
            public void set(final AsyncCallback<String> callback) {
                asyncBean.hello(PARAMETER, callback);
            }
        }, new AsyncCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(syncBean.hello(PARAMETER), result);
            }
        });
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertEquals(syncBean.hello(PARAMETER, PARAMETER), asyncBean.hello(PARAMETER, PARAMETER).get());

        check(new Consumer<AsyncCallback<String>>() {
            public void set(final AsyncCallback<String> callback) {
                asyncBean.hello(PARAMETER, PARAMETER, callback);
            }
        }, new AsyncCallbackAdapter<String>() {
            @Override
            public void onSuccess(final String result) {
                Assert.assertEquals(syncBean.hello(PARAMETER, PARAMETER), result);
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionFuture() throws Throwable {
        syncBean.exception();
        Assert.fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionCallback() throws Throwable {
        check(new Consumer<AsyncCallback<Void>>() {
            public void set(final AsyncCallback<Void> callback) {
                asyncBean.exception(callback);
            }
        }, new AsyncCallbackAdapter<Void>());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectProxy() throws Throwable {
        Assert.fail(String.valueOf(AsyncProxyCreator.create(syncBean, List.class, executorService)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectProxyOnDemandFuture() throws Throwable {
        Assert.fail(String.valueOf(asyncBean.methodWithoutSyncImpl()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectProxyOnDemandCallback() throws Throwable {
        asyncBean.methodWithoutSyncImpl(new AsyncCallbackAdapter<Boolean>());
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

    /**
     * @author Vladislav Bauer
     * @param <T> class type
     */
    private interface Consumer<T> {

        void set(T input);

    }

}
