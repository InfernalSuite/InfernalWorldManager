package com.infernalsuite.api.event.listeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to indicate this method should be registered as an event handler with the IWM Event Bus.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * The order events will be posted to this listener.
     *
     * @return the order
     */
    PostOrder order() default PostOrder.NORMAL;

    /**
     * Whether the handler must be called asynchronously.
     *
     * @return requires async
     */
    boolean async() default true;

}
