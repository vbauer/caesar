package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public class FutureMethodRunnerTest extends BasicRunnerTest {

    @Test(expected = CancellationException.class)
    public void testTimeout() throws Throwable {
        final Future<Boolean> future = getFutureAsync().timeout();
        Assert.assertNotNull(future);
        Assert.fail(String.valueOf(future.get()));
    }

    @Test
    public void testWithoutResult() throws Throwable {
        getSync().empty();

        final Future<Void> future = getFutureAsync().empty();
        Assert.assertNotNull(future);
        Assert.assertNull(future.get());
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        getSync().emptyHello(PARAMETER);

        final Future<Void> future = getFutureAsync().emptyHello(PARAMETER);
        Assert.assertNotNull(future);
        Assert.assertNull(future.get());
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertEquals(getSync().hello(PARAMETER), getFutureAsync().hello(PARAMETER).get());
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertEquals(getSync().hello(PARAMETER, PARAMETER), getFutureAsync().hello(PARAMETER, PARAMETER).get());
    }

    @Test(expected = ExecutionException.class)
    public void testException() throws Throwable {
        getFutureAsync().exception().get();
        Assert.fail();
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() {
        Assert.fail(String.valueOf(getFutureAsync().methodWithoutSyncImpl()));
    }

}
