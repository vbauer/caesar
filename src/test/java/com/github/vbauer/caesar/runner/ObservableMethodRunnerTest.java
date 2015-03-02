package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.basic.BasicRunnerTest;
import com.github.vbauer.caesar.exception.MissedSyncMethodException;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;

/**
 * @author Vladislav Bauer
 */

public class ObservableMethodRunnerTest extends BasicRunnerTest {

    @Test
    public void testWithoutResult() throws Throwable {
        getSync().empty();

        final Observable<Void> observable = getObservableAsync().empty();
        Assert.assertNotNull(observable);
        Assert.assertNull(observable.toBlocking().first());
    }

    @Test
    public void test1ArgumentWithoutResult() throws Throwable {
        getSync().emptyHello(PARAMETER);

        final Observable<Void> observable = getObservableAsync().emptyHello(PARAMETER);
        Assert.assertNotNull(observable);
        Assert.assertNull(observable.toBlocking().first());
    }

    @Test
    public void test1ArgumentWithResult() throws Throwable {
        Assert.assertEquals(
            getSync().hello(PARAMETER),
            getObservableAsync().hello(PARAMETER).toBlocking().first()
        );
    }

    @Test
    public void test2ArgumentsWithResult() throws Throwable {
        Assert.assertEquals(
            getSync().hello(PARAMETER, PARAMETER),
            getObservableAsync().hello(PARAMETER, PARAMETER).toBlocking().first()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testException() throws Throwable {
        getObservableAsync().exception().toBlocking().first();
        Assert.fail();
    }

    @Test(expected = MissedSyncMethodException.class)
    public void testIncorrectProxyOnDemand() throws Throwable {
        Assert.fail(String.valueOf(getObservableAsync().methodWithoutSyncImpl()));
    }

}
