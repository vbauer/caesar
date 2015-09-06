package com.github.vbauer.caesar.annotation;

import com.github.vbauer.caesar.basic.BasicTest;
import com.github.vbauer.caesar.bean.SimpleAsync;
import com.github.vbauer.caesar.bean.SimpleSync;
import com.github.vbauer.caesar.exception.UnsupportedTimeoutException;
import com.github.vbauer.caesar.proxy.AsyncProxyCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vladislav Bauer
 */

public class TimeoutAnnotationTest extends BasicTest {

    @Test
    public void testGoodExecutorService() throws Exception {
        final ExecutorService executor = Executors.newScheduledThreadPool(1);
        try {
            final SimpleAsync proxy = createProxy(executor);
            Assert.assertEquals(SimpleSync.TIMEOUT_VALUE, proxy.timeout().get());
        } finally {
            executor.shutdown();
        }
    }

    @Test(expected = UnsupportedTimeoutException.class)
    public void testBadExecutorService() throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        try {
            final SimpleAsync proxy = createProxy(executor);
            Assert.fail(String.valueOf(proxy.timeout().get()));
        } finally {
            executor.shutdown();
        }
    }

    private SimpleAsync createProxy(final ExecutorService executor) {
        final SimpleSync bean = new SimpleSync(1);
        return AsyncProxyCreator.create(bean, SimpleAsync.class, executor);
    }

}
