package com.github.vbauer.caesar.exception;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("serial")
public abstract class AbstractCaesarException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Some problem has happened during Caesar work";
    }

}
