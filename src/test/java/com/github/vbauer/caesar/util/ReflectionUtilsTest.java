package com.github.vbauer.caesar.util;

import com.github.vbauer.caesar.basic.BasicTest;
import com.github.vbauer.caesar.runner.AsyncMethodRunnerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Vladislav Bauer
 */

public class ReflectionUtilsTest extends BasicTest {

    @Test
    public void testConstructorContract() throws Exception {
        checkUtilConstructorContract(ReflectionUtils.class);
    }

    @Test
    public void testCreateObject() {
        Assert.assertEquals(Object.class, ReflectionUtils.createObject("java.lang.Object").getClass());
        Assert.assertEquals(null, ReflectionUtils.createObject(null));
    }

    @Test
    public void testCreateObjects() {
        final Collection<Object> objects =
            ReflectionUtils.createObjects(Arrays.asList("java.lang.Object"));
        
        final Object firstElement = objects.iterator().next();
        Assert.assertEquals(Object.class, firstElement.getClass());
        Assert.assertEquals(1, objects.size());
    }

    @Test
    public void testClassNames() {
        final String packageName = AsyncMethodRunnerFactory.PACKAGE_NAME;
        final String[] classNames = AsyncMethodRunnerFactory.CLASS_NAMES;
        final Collection<String> result =
            ReflectionUtils.classNames(packageName, Arrays.asList(classNames));

        Assert.assertEquals(classNames.length, result.size());
        Assert.assertEquals(
            packageName + ReflectionUtils.PACKAGE_SEPARATOR + classNames[0],
            result.iterator().next()
        );
    }
    
}
