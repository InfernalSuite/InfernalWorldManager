package com.infernalsuite.iwm.api.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;

/**
 * Registry for all loaded worlds within IWM
 */
public interface WorldRegistry {

    /**
     * Gets a {@link InfernalWorld} by its registered name
     *
     * @param worldName the name of the world
     * @return an {@link Optional} containing the world, or {@link Optional#empty()} if the given name doesn't match a registered world
     */
    @NonNull Optional<InfernalWorld> getWorld(@NonNull String worldName);

    /**
     * Register an {@link InfernalWorld} with the world registry.
     *
     * @param world the world to register
     */
    void registerWorld(@NonNull InfernalWorld world);



}
