package com.github.vbauer.caesar.util;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void testCreateObject() {
        Assert.assertEquals(Object.class, ReflectionUtils.createObject("java.lang.Object").getClass());
        Assert.assertEquals(null, ReflectionUtils.createObject(null));
    }

    @Test
    public void testCreateObjects() {
        final List<Object> objects = ReflectionUtils.createObjects("java.lang.Object");
        Assert.assertEquals(1, objects.size());
        Assert.assertEquals(Object.class, objects.get(0).getClass());
    }

}
