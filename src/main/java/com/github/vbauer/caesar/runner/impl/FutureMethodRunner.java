package com.github.vbauer.caesar.runner.impl;

import com.github.vbauer.caesar.runner.impl.base.AbstractReturnMethodRunner;

import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("all")
public class FutureMethodRunner extends AbstractReturnMethodRunner {

    @Override
    protected Class<?> getReturnClass() {
        return Future.class;
    }

}
