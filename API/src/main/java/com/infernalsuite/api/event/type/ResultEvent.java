package com.infernalsuite.api.event.type;

import com.infernalsuite.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents an event that has a result.
 *
 * @param <T> the type of the result
 */
public interface ResultEvent<T> {

    /**
     * Gets an {@link AtomicReference} containing the result.
     *
     * @return the result
     */
    @Param(-1)
    @NonNull AtomicReference<T> result();

    /**
     * Checks if a result has been set for the event.
     *
     * @return {@code true} if there is a result
     */
    default boolean hasResult() {
        return result().get() != null;
    }

    /**
     * Get the result from {@link #result()}
     *
     * @return the result contained within the {@link AtomicReference} in this event, or {@code null} if no result has been set
     */
    default @Nullable T getResult() {
        return result().get();
    }

}
