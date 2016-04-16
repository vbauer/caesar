package com.github.vbauer.caesar.exception;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Vladislav Bauer
 */

@SuppressWarnings("serial")
public class MissedSyncMethodException extends AbstractCaesarException {

    private final Method method;
    private final Object[] arguments;


    public MissedSyncMethodException(final Method method, final Object... arguments) {
        this.method = method;
        this.arguments = arguments;
    }


    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return String.format(
            "Can not find appropriate sync-method \"%s\", parameters: %s",
            getMethod(), Arrays.toString(getArguments())
        );
    }

}
