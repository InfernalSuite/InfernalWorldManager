package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.loaders.IWMLoader;

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



}
