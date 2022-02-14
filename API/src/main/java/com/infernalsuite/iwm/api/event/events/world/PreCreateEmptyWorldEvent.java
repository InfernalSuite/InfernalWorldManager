package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before an empty world is created.
 *
 * <p>Contains the name of the new world, its loader, whether it's read only and the world properties.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the creation process.</p>
 */
public interface PreCreateEmptyWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the new world.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Gets the loader being used for the new world.
     *
     * @return the loader
     */
    @Param(1)
    @NonNull IWMLoader getLoader();

    /**
     * Gets whether the new world is read only.
     *
     * @return {@code true} if the world is read only
     */
    @Param(2)
    boolean isReadOnly();

    /**
     * Gets the world properties being used for the new world.
     *
     * @return the world properties
     */
    @Param(3)
    @NonNull WorldProperties getWorldProperties();

}
