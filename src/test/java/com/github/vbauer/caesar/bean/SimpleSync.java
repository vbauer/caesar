package com.github.vbauer.caesar.bean;

/**
 * @author Vladislav Bauer
 */

public class SimpleSync {

    public static final boolean TIMEOUT_VALUE = true;
    private final int id;


    public SimpleSync(final int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public Boolean timeout() throws InterruptedException {
        Thread.sleep(1000);
        return TIMEOUT_VALUE;
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SimpleSync) {
            final SimpleSync other = (SimpleSync) obj;
            return other.getId() == getId();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

}
