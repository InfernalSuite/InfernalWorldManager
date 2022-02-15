package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;

/**
 * A representation of a world within IWM
 */
public interface InfernalWorld {

    /**
     * Gets the name of the world.
     *
     * @return the world name
     */
    String getName();

    /**
     * Gets the loader used to load and save this world.
     *
     * @return the loader
     */
    IWMLoader getLoader();

    /**
     * Gets the {@link WorldProperties} contains all the properties of the world.
     *
     * @return the world properties
     */
    WorldProperties getWorldProperties();

    /**
     * Gets whether the world is read only.
     *
     * @return {@code true} if the world is read only
     */
    boolean isReadOnly();

    /**
     * Gets whether the world is locked.
     *
     * @return {@code true} if the world is locked
     */
    boolean isLocked();

}
