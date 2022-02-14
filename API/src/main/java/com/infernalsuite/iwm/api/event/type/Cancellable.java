package com.infernalsuite.iwm.api.event.type;

import com.infernalsuite.iwm.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents an event that can be cancelled.
 */
public interface Cancellable {

    /**
     * Gets an {@link AtomicBoolean} holding the cancellation state of the event
     *
     * @return the cancellation state
     */
    @Param(-1)
    @NonNull AtomicBoolean cancellationState();

    /**
     * Returns true if the event is currently cancelled.
     *
     * @return {@code true} if the event is cancelled
     */
    default boolean isCancelled() {
        return cancellationState().get();
    }

    /**
     * Returns true if the event is not currently cancelled.
     *
     * @return {@code true} if the event is not cancelled
     */
    default boolean isNotCancelled() {
        return !isCancelled();
    }

    /**
     * Sets the cancellation state of the event.
     *
     * @param cancelled the new state
     * @return the previous state
     */
    default boolean setCancelled(boolean cancelled) {
        return cancellationState().getAndSet(cancelled);
    }

}
