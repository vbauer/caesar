package com.github.vbauer.caesar.exception;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Modifier;

/**
 * @author Vladislav Bauer
 */

public class CaesarExceptionTest extends BasicTest {

    @Test(expected = RuntimeException.class)
    public void testAbstractCaesarException() {
        final Class<AbstractCaesarException> clazz = AbstractCaesarException.class;
        final int modifiers = clazz.getModifiers();
        Assert.assertTrue(Modifier.isAbstract(modifiers));

        final AbstractCaesarException ex = new AbstractCaesarException() {};
        checkCommon(ex, RuntimeException.class);
    }

    @Test(expected = AbstractCaesarException.class)
    public void testMissedSyncMethodException() {
        final MissedSyncMethodException ex = new MissedSyncMethodException(null);
        Assert.assertEquals(0, ex.getArguments().length);
        Assert.assertNull(ex.getMethod());
        checkCommon(ex, AbstractCaesarException.class);
    }

    @Test(expected = AbstractCaesarException.class)
    public void testUnsupportedTimeoutException() {
        final UnsupportedTimeoutException ex = new UnsupportedTimeoutException(null);
        Assert.assertNull(ex.getExecutor());
        checkCommon(ex, AbstractCaesarException.class);
    }


    private void checkCommon(final RuntimeException ex, final Class<?> parentClass) {
        Assert.assertFalse(ex.getMessage().isEmpty());
        Assert.assertTrue(parentClass.isAssignableFrom(ex.getClass().getSuperclass()));
        throw ex;
    }

}
