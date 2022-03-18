package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a world is registered with the {@link WorldRegistry world registry}
 *
 * <p>Contains the world name and the {@link InfernalWorld world} to be registered.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the world registration process.</p>
 */
public interface PreRegisterWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the world to be registered.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Gets the {@link InfernalWorld world} to be registered.
     *
     * @return the world
     */
    @Param(1)
    @NonNull InfernalWorld getWorld();

}
