package com.github.vbauer.caesar.proxy;

import com.github.vbauer.caesar.basic.BasicTest;
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

    private static ExecutorService executorService;


    @BeforeClass
    public static void setUp() {
        executorService = Executors.newFixedThreadPool(5);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        executorService.shutdown();
    }


    @Test
    public void testConstructorContract() throws Exception {
        checkUtilConstructorContract(AsyncProxyCreator.class);
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxy() throws Throwable {
        Assert.fail(String.valueOf(AsyncProxyCreator.create(new Sync(), List.class, executorService)));
    }

}
