package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a world is unregistered from the {@link WorldRegistry world registry}
 *
 * <p>Contains the name of the world being unregistered.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the de-registration process.</p>
 */
public interface PreUnregisterWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the world being unregistered.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

}
