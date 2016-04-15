package com.github.vbauer.caesar.runner;

import com.github.vbauer.caesar.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Vladislav Bauer 
 */

public final class AsyncMethodRunnerFactory {

    public static final String PACKAGE_NAME = "com.github.vbauer.caesar.runner.impl";

    /**
     * Immutable list with class names of method runners.
     *
     * IMPORTANT:
     * This classes must be listed in the right order: from more-specific to less-specific classes.
     */
    public static final List<String> CLASS_NAMES = Collections.unmodifiableList(
        Arrays.asList(
            "ObservableMethodRunner",
            "ListenableFutureMethodRunner",
            "FutureCallbackMethodRunner",
            "FutureMethodRunner",
            "AsyncCallbackMethodRunner",
            "SyncMethodRunner"
        )
    );
    

    private AsyncMethodRunnerFactory() {
        throw new UnsupportedOperationException();
    }


    public static Collection<AsyncMethodRunner> createMethodRunners() {
        final Collection<String> classNames =
            ReflectionUtils.classNames(PACKAGE_NAME, CLASS_NAMES);
        
        return ReflectionUtils.createObjects(classNames);
    }
    
}
