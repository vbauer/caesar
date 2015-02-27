package com.github.vbauer.caesar.basic;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Vladislav Bauer
 */

@RunWith(BlockJUnit4ClassRunner.class)
public abstract class BasicTest {

    protected final void checkUtilConstructorContract(final Class<?> utilClass) throws Exception {
        try {
            Assert.fail(utilClass.newInstance().toString());
        } catch (final IllegalAccessException ex) {
            final Constructor<?> constructor = utilClass.getDeclaredConstructor();
            constructor.setAccessible(true);

            try {
                Assert.fail(constructor.newInstance().toString());
            } catch (final InvocationTargetException e) {
                final Throwable targetException = e.getTargetException();
                Assert.assertEquals(UnsupportedOperationException.class, targetException.getClass());
            }
        }
    }

}
