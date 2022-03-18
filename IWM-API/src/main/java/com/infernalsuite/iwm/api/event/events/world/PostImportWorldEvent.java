package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called after a world has been imported into IWM.
 *
 * <p>Contains the name of the world which has been imported.</p>
 */
public interface PostImportWorldEvent extends IWMEvent {

    /**
     * Gets the name of the world which has been imported.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

}
