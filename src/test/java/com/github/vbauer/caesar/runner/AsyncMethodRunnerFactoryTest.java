package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Vladislav Bauer 
 */

public class AsyncMethodRunnerFactoryTest extends BasicTest {

    @Test
    public void testConstructorContract() throws Exception {
        checkUtilConstructorContract(AsyncMethodRunnerFactory.class);
    }
    
    @Test
    public void testCreateMethodRunners() {
        final Collection<AsyncMethodRunner> methodRunners =
            AsyncMethodRunnerFactory.createMethodRunners();

        final Iterator<AsyncMethodRunner> iterator = methodRunners.iterator();
        final int runnersCount = AsyncMethodRunnerFactory.CLASS_NAMES.length;

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < runnersCount; i++) {
            Assert.assertNotNull(iterator.next());
        }
        Assert.assertEquals(runnersCount, methodRunners.size());
    }
    
}
