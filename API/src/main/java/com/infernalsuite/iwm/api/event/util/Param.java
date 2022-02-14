package com.infernalsuite.iwm.api.event.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the position of a parameter within an event
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Param {

    /**
     * Gets the index of the parameter.
     *
     * @return the index
     */
    int value();
}
