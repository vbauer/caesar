package com.github.vbauer.caesar.basic;

import com.pushtorefresh.private_constructor_checker.PrivateConstructorChecker;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Vladislav Bauer
 */

@RunWith(BlockJUnit4ClassRunner.class)
public abstract class BasicTest {

    protected final boolean checkUtilConstructorContract(final Class<?>... utilClasses) throws Exception {
        Assert.assertTrue(utilClasses.length > 0);

        PrivateConstructorChecker
            .forClasses(utilClasses)
            .expectedTypeOfException(UnsupportedOperationException.class)
            .check();

        return true;
    }

}
