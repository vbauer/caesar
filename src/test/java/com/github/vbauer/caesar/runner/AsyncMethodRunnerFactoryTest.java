package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * @author Vladislav Bauer 
 */

public class AsyncMethodRunnerFactoryTest extends BasicTest {

    @Test
    public void testConstructorContract() throws Exception {
        Assert.assertTrue(checkUtilConstructorContract(AsyncMethodRunnerFactory.class));
    }
    
    @Test
    public void testCreateMethodRunners() {
        final int runnersCount = AsyncMethodRunnerFactory.CLASS_NAMES.size();
        final Collection<AsyncMethodRunner> methodRunners =
            AsyncMethodRunnerFactory.createMethodRunners();

        Assert.assertEquals(runnersCount, methodRunners.size());
        for (final AsyncMethodRunner methodRunner : methodRunners) {
            Assert.assertNotNull(methodRunner);
        }
    }
    
}
