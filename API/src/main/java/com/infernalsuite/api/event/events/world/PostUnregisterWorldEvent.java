package com.infernalsuite.api.event.events.world;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once world de-registration has been completed.
 *
 * <p>Contains the name of the world which has been unregistered.</p>
 */
public interface PostUnregisterWorldEvent extends IWMEvent {

    /**
     * Gets the name of the world which has been unregistered.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

}
