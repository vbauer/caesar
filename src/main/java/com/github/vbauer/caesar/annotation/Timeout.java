package com.github.vbauer.caesar.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Timeout annotation allows to stop operation after some period.
 *
 * @author Vladislav Bauer
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Timeout {

    /**
     * @return timeout value
     */
    long value();

    /**
     * @return time unit
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}
