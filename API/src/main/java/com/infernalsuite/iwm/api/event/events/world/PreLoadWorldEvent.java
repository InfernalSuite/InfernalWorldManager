package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a world is loaded.
 *
 * <p>Contains the {@link IWMLoader loader} being used to load the world, the world name, whether the world is read only
 * and the {@link WorldProperties properties}</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the world loading process.</p>
 */
public interface PreLoadWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the {@link IWMLoader} being used to load the world
     *
     * @return the loader
     */
    @Param(0)
    @NonNull IWMLoader getLoader();

    /**
     * Gets the name of the world being loaded
     *
     * @return the world name
     */
    @Param(1)
    @NonNull String getWorldName();

    /**
     * Gets if the world being loaded is read only
     *
     * @return {@code true} if the world is read only
     */
    @Param(2)
    boolean isReadOnly();

    /**
     * Gets the world properties for the world being loaded
     *
     * @return the world properties
     */
    @Param(3)
    @NonNull WorldProperties getWorldProperties();

}
