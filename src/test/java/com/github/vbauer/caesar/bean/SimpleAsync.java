package com.github.vbauer.caesar.bean;

import com.github.vbauer.caesar.callback.AsyncCallback;

/**
 * @author Vladislav Bauer
 */

public interface SimpleAsync {

    void hello(AsyncCallback<String> callback, String name);

}
