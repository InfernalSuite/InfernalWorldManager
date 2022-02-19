package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Registry for all loaded worlds within IWM
 */
public interface WorldRegistry {

    /**
     * Register an {@link InfernalWorld} with the world registry.
     *
     * @param world the world to register
     */
    @NonNull CompletableFuture<Void> register(@NonNull InfernalWorld world);

    /**
     * Gets a {@link InfernalWorld} by its registered name
     *
     * @param worldName the name of the world
     * @return an {@link Optional} containing the world, or {@link Optional#empty()} if the given name doesn't match a registered world
     */
    @NonNull CompletableFuture<Optional<InfernalWorld>> getWorld(@NonNull String worldName);

    /**
     * Unregister the given {@link InfernalWorld} from the {@link WorldRegistry registry}
     *
     * @param world the world to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull InfernalWorld world);

    /**
     * Unregister an {@link InfernalWorld} using the world's registered name.
     *
     * @param worldName the name of the world to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull String worldName);

    /**
     * Gets a list of names of the worlds registered with this registry.
     *
     * @return a list of registered worlds names
     */
    @NonNull List<String> getRegisteredWorlds();

    /**
     * Loads a world from the given loader using its world name.
     *
     * @param loader the {@link IWMLoader loader} from which to load the world
     * @param worldName the name of the world to load
     * @param readOnly whether the world should be read only
     * @param properties the world properties which should be used for this world
     * @return an {@link Optional} containing the world, or {@link Optional#empty()} if no world was loaded
     */
    @NonNull CompletableFuture<Optional<InfernalWorld>> loadWorld(@NonNull IWMLoader loader, @NonNull String worldName, boolean readOnly, @NonNull WorldPropertyMap properties);

    /**
     * Creates an empty world.
     *
     * @param worldName the new name for the world
     * @param loader the loader to use
     * @param readOnly whether the world should be read only
     * @param properties the world properties for the world
     * @return an {@link Optional} containing the world, or {@link Optional#empty()} if no world was created
     */
    @NonNull CompletableFuture<Optional<InfernalWorld>> createEmptyWorld(@NonNull String worldName, @NonNull IWMLoader loader, boolean readOnly, @NonNull WorldPropertyMap properties);

    /**
     * Migrates a world from one loader to another.
     *
     * @param worldName the name of the world to migrate
     * @param currentLoader the loader to migrate the world from
     * @param newLoader the loader to migrate the world to
     */
    @NonNull CompletableFuture<Void> migrateWorld(@NonNull String worldName, @NonNull IWMLoader currentLoader, @NonNull IWMLoader newLoader);

    /**
     * Imports a world into IWM.
     *
     * @param worldDir the world directory to import the world from
     * @param worldName the name of the world to import
     * @param loader the loader to use
     */
    @NonNull CompletableFuture<Void> importWorld(@NonNull File worldDir, @NonNull String worldName, @NonNull IWMLoader loader);

    /**
     * Generates a {@link InfernalWorld world} (adds it to the server's world list).
     *
     * @param world the world to generate
     */
    void generateWorld(@NonNull InfernalWorld world);

}
