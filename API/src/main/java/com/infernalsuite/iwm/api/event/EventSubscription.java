package com.infernalsuite.iwm.api.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

/**
 * Represents a subscription to an {@link IWMEvent}
 *
 * @param <T> the type of the event
 */
public interface EventSubscription<T extends IWMEvent> extends AutoCloseable {

    /**
     * Gets the class the handler is listening to.
     *
     * @return the event class
     */
    @NonNull Class<T> getEventClass();

    /**
     * Returns true if the handler is active.
     *
     * @return {@code true} if the handler is still active
     */
    boolean isActive();

    /**
     * Unregisters the handler from the event bus.
     */
    @Override
    void close();

    /**
     * Gets the event consumer responsible for handling the event.
     *
     * @return the event consumer
     */
    @NonNull Consumer<? super T> getHandler();
}
