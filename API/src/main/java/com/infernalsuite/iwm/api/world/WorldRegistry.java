package com.infernalsuite.iwm.api.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Registry for all loaded worlds within IWM
 */
public interface WorldRegistry {

    /**
     * Register an {@link InfernalWorld} with the world registry.
     *
     * @param world the world to register
     */
    void register(@NonNull InfernalWorld world);

    /**
     * Gets a {@link InfernalWorld} by its registered name
     *
     * @param worldName the name of the world
     * @return an {@link Optional} containing the world, or {@link Optional#empty()} if the given name doesn't match a registered world
     */
    @NonNull Optional<InfernalWorld> getWorld(@NonNull String worldName);

    /**
     * Unregister the given {@link InfernalWorld} from the {@link WorldRegistry registry}
     *
     * @param world the world to unregister
     */
    void unregister(@NonNull InfernalWorld world);

    /**
     * Unregister an {@link InfernalWorld} using the world's registered name.
     *
     * @param world the name of the world to unregister
     */
    void unregister(@NonNull String world);

    /**
     * Gets a list of names of the worlds registered with this registry.
     *
     * @return a list of registered worlds names
     */
    @NonNull List<String> getRegisteredWorlds();



}
