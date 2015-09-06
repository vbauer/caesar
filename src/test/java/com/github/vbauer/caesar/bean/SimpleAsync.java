package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.annotation.Timeout;

import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

@Timeout(5)
public interface SimpleAsync {

    Future<Integer> getId();

    Future<Boolean> timeout();

}
