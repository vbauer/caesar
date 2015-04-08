package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * @author Vladislav Bauer
 */

public class ListenableFutureMethodRunnerTest extends BasicRunnerTest {

    @Test(expected = CancellationException.class)
    public void testTimeout() throws Throwable {
        final ListenableFuture<Boolean> future = getListenableFutureAsync().timeout();
        Assert.assertNotNull(future);
        Assert.fail(String.valueOf(future.get()));
    }

    @Test
    public void testWithoutResult() throws Throwable {
        getSync().empty();

        final ListenableFuture<Void> future = getListenableFutureAsync().empty();
        Assert.assertNotNull(future);
        Assert.assertNull(future.get());
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        getSync().emptyHello(PARAMETER);

        final ListenableFuture<Void> future = getListenableFutureAsync().emptyHello(PARAMETER);
        Assert.assertNotNull(future);
        Assert.assertNull(future.get());
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertEquals(
            getSync().hello(PARAMETER),
            getListenableFutureAsync().hello(PARAMETER).get()
        );
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertEquals(
            getSync().hello(PARAMETER, PARAMETER),
            getListenableFutureAsync().hello(PARAMETER, PARAMETER).get()
        );
    }

    @Test(expected = ExecutionException.class)
    public void testException() throws Throwable {
        getListenableFutureAsync().exception().get();
        Assert.fail();
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() throws Throwable {
        Assert.fail(String.valueOf(getListenableFutureAsync().methodWithoutSyncImpl()));
    }

}
