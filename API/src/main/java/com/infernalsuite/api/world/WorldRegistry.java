package com.infernalsuite.api.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Registry for all loaded worlds within IWM
 */
public interface WorldRegistry {

    /**
     * Retrieve a world from the world registry by name.
     *
     * @param worldName the name of the world
     * @return an {@link InfernalWorld} object for the given world name, or {@code null} if the given name doesn't match
     */
    @Nullable InfernalWorld getWorld(@NonNull String worldName);

    /**
     * Register a world with the world registry.
     *
     * @param world the world to register
     */
    void registerWorld(@NonNull InfernalWorld world);



}
