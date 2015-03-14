package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Vladislav Bauer 
 */

public final class AsyncMethodRunnerFactory {

    public static final String PACKAGE_NAME = "com.github.vbauer.caesar.runner.impl";

    public static final String[] CLASS_NAMES = {
        "AsyncCallbackMethodRunner",
        "FutureMethodRunner",
        "FutureCallbackMethodRunner",
        "ListenableFutureMethodRunner",
        "ObservableMethodRunner",
    };
    

    private AsyncMethodRunnerFactory() {
        throw new UnsupportedOperationException();
    }


    public static Collection<AsyncMethodRunner> createMethodRunners() {
        final Collection<String> classNames =
            ReflectionUtils.classNames(PACKAGE_NAME, Arrays.asList(CLASS_NAMES));
        
        return ReflectionUtils.createObjects(classNames);
    }
    
}
