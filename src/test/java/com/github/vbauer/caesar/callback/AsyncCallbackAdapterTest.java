package com.github.vbauer.caesar.callback;

import com.github.vbauer.caesar.basic.BasicTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vladislav Bauer
 */

public class AsyncCallbackAdapterTest extends BasicTest {

    @Test
    public void testCallback() {
        final AtomicBoolean onSuccess = new AtomicBoolean(false);
        final AtomicBoolean onFailure = new AtomicBoolean(false);

        final AsyncCallbackAdapter<Object> callback = new AsyncCallbackAdapter<Object>() {
            @Override
            public void onSuccess(final Object result) {
                super.onSuccess(result);
                onSuccess.set(true);
            }

            @Override
            public void onFailure(final Throwable caught) {
                super.onFailure(caught);
                onFailure.set(true);
            }
        };

        callback.onSuccess(null);
        callback.onFailure(null);

        Assert.assertTrue(onSuccess.get());
        Assert.assertTrue(onFailure.get());
    }

}
