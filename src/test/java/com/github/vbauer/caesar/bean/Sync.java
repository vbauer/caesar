package com.github.vbauer.caesar.bean;

/**
 * @author Vladislav Bauer
 */

public class Sync {

    public String hello(final String name) {
        return String.format("Hello, %s", name);
    }

    public String hello(final String name1, final String name2) {
        return String.format("Hello, %s and %s", name1, name2);
    }

    public void empty() {
        // Do nothing.
    }

    public void emptyHello(@SuppressWarnings("unused") final String name) {
        // Do nothing.
    }

    public void exception() {
        throw new UnsupportedOperationException();
    }

    public Boolean timeout() throws InterruptedException {
        Thread.sleep(1000);
        return true;
    }

}
