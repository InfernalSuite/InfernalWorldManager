package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Collection;
import java.util.Map;

/**
 * A representation of a world within IWM
 */
public interface InfernalWorld {

    /**
     * Gets the name of the world.
     *
     * @return the world name
     */
    @NonNull String getName();

    /**
     * Gets the loader used to load and save this world.
     *
     * @return the loader
     */
    @NonNull IWMLoader getLoader();

    /**
     * Gets the chunk at the specified co-ordinates.
     *
     * @param cx the chunk's X co-ordinate
     * @param cz the chunk's Z co-ordinate
     * @return the {@link InfernalChunk} at the given co-ordinates
     */
    @NonNull InfernalChunk getChunk(int cx, int cz);

    /**
     * Returns a {@link Map} with every {@link InfernalChunk} that is currently loaded.
     *
     * @return a map containing every loaded chunk
     */
    Map<Long, InfernalChunk> getChunks();

    /**
     * Returns the extra data of the world.
     *
     * @return a {@link NBTCompound} containing any extra data of the world
     */
    NBTCompound getExtraData();

    /**
     * Returns a {@link Collection} containing every world map, serialized in a {@link NBTCompound}.
     *
     * @return a collection of serialized world maps
     */
    Collection<NBTCompound> getWorldMaps();

    /**
     * Gets the {@link WorldProperties} contains all the properties of the world.
     *
     * @return the world properties
     */
    @NonNull WorldProperties getWorldProperties();

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
