package com.github.vbauer.caesar.util;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Test;

/**
 * @author Vladislav Bauer
 */

public class ReflectionUtilsTest extends BasicTest {

    @Test
    public void testConstructorContract() throws Exception {
        checkUtilConstructorContract(ReflectionUtils.class);
    }

    @Test(expected = RuntimeException.class)
    public void testHandleReflectionException() {
        ReflectionUtils.handleReflectionException(new Exception());
    }

    @Test(expected = RuntimeException.class)
    public void testHandleReflectionRuntimeException() {
        ReflectionUtils.handleReflectionException(new RuntimeException());
    }

}
