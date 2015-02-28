package com.github.vbauer.caesar.exception;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vladislav Bauer
 */

public class MissedSyncMethodExceptionTest extends BasicTest {

    @Test(expected = MissedSyncMethodException.class)
    public void testException() {
        final MissedSyncMethodException ex = new MissedSyncMethodException(null);

        Assert.assertEquals(0, ex.getArguments().length);
        Assert.assertNull(ex.getMethod());
        Assert.assertNotNull(ex.getMessage());

        throw ex;
    }

}
