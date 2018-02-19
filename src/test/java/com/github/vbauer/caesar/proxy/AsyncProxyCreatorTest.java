package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.basic.BasicTest;
import com.github.vbauer.caesar.bean.CallbackAsync;
import com.github.vbauer.caesar.bean.SimpleAsync;
import com.github.vbauer.caesar.bean.SimpleSync;
import com.github.vbauer.caesar.bean.Sync;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vladislav Bauer
 */

public class AsyncProxyCreatorTest extends BasicTest {

    private static final ThreadLocal<ExecutorService> EXECUTOR = new ThreadLocal<>();


    @BeforeClass
    public static void setUp() {
        EXECUTOR.set(Executors.newScheduledThreadPool(5));
    }

    @AfterClass
    public static void tearDown() {
        executor().shutdown();
    }

    @Test
    public void testConstructorContract() {
        Assert.assertTrue(checkUtilConstructorContract(AsyncProxyCreator.class));
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxy() {
        Assert.fail(String.valueOf(AsyncProxyCreator.create(new Sync(), List.class, executor())));
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testBadProxy() {
        Assert.fail(String.valueOf(
            AsyncProxyCreator.create(new Sync(), CallbackAsync.class, executor())
        ));
    }

    @Test
    public void testCorrectProxy() throws Throwable {
        final SimpleSync bean1 = new SimpleSync(1);
        final SimpleAsync proxy1 = AsyncProxyCreator.create(bean1, SimpleAsync.class, executor());
        Assert.assertNotNull(proxy1);
        Assert.assertEquals(bean1.getId(), (int) proxy1.getId().get());

        final SimpleSync bean2 = new SimpleSync(2);
        final SimpleAsync proxy2 = AsyncProxyCreator.create(bean2, SimpleAsync.class, executor());
        Assert.assertNotNull(proxy2);
        Assert.assertEquals(bean2.getId(), (int) proxy2.getId().get());

        Assert.assertNotEquals(proxy1, proxy2);
        Assert.assertNotEquals(proxy1.hashCode(), proxy2.hashCode());
        Assert.assertNotEquals(proxy1.toString(), proxy2.toString());
        Assert.assertNotNull(proxy1.toString());
    }


    private static ExecutorService executor() {
        return EXECUTOR.get();
    }

}
