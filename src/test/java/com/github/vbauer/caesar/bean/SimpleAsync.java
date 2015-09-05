package com.github.vbauer.caesar.bean;

import java.util.concurrent.Future;

/**
 * @author Vladislav Bauer
 */

public interface SimpleAsync {

    Future<Integer> getId();

}
