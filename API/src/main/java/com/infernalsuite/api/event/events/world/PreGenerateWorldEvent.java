package com.infernalsuite.api.event.events.world;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a world is generated.
 *
 * <p>Contains the name of the world to be generated, and the respective {@link InfernalWorld world} object.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the world generation process.</p>
 */
public interface PreGenerateWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the world to be generated.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Get the {@link InfernalWorld world} to be generated.
     *
     * @return the world
     */
    @Param(1)
    @NonNull InfernalWorld getWorld();

}
