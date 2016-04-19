package com.github.vbauer.caesar.util;

import com.github.vbauer.caesar.basic.BasicTest;
import com.github.vbauer.caesar.runner.AsyncMethodRunnerFactory;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Vladislav Bauer
 */

public class ReflectionUtilsTest extends BasicTest {

    private static final String CLASS_OBJECT = "java.lang.Object";
    private static final String CLASS_STRING = "java.lang.String";


    @Test
    public void testConstructorContract() throws Exception {
        Assert.assertTrue(checkUtilConstructorContract(ReflectionUtils.class));
    }

    @Test
    public void testCreateObject() {
        Assert.assertEquals(
            Object.class,
            ReflectionUtils.getClassSafe(ReflectionUtils.createObject(CLASS_OBJECT))
        );
        Assert.assertEquals(null, ReflectionUtils.createObject(null));
    }

    @Test
    public void testCreateObjects() {
        final List<Object> objects = Lists.newArrayList(
            ReflectionUtils.createObjects(
                Arrays.asList(CLASS_OBJECT, CLASS_STRING)
            )
        );
        
        Assert.assertEquals(Object.class, objects.get(0).getClass());
        Assert.assertEquals(String.class, objects.get(1).getClass());
        Assert.assertEquals(2, objects.size());
    }

    @Test
    public void testClassNames() {
        final String packageName = AsyncMethodRunnerFactory.PACKAGE_NAME;
        final List<String> classNames = AsyncMethodRunnerFactory.CLASS_NAMES;
        final Collection<String> result =
            ReflectionUtils.classNames(packageName, classNames);

        Assert.assertEquals(classNames.size(), result.size());
        Assert.assertEquals(
            packageName + ReflectionUtils.PACKAGE_SEPARATOR + classNames.get(0),
            result.iterator().next()
        );
    }

    @Test
    public void testGetClassSafe() {
        Assert.assertNull(ReflectionUtils.getClassSafe(null));
        Assert.assertEquals(
            String.class, ReflectionUtils.getClassSafe(ReflectionUtils.PACKAGE_SEPARATOR)
        );
    }

}
