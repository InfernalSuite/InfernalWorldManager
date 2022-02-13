package com.infernalsuite.api.event;

import com.infernalsuite.api.InfernalWorldManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Parent interface for all IWM Events.
 */
public interface IWMEvent {

    /**
     * Get the API instance this event was dispatched from.
     *
     * @return the API instance
     */
    @NonNull InfernalWorldManager getIWM();

    /**
     * Get the type of the event.
     *
     * @return the type of the event
     */
    @NonNull Class<? extends IWMEvent> getEventType();

}
