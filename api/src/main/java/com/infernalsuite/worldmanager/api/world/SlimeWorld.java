package com.infernalsuite.worldmanager.api.world;

import com.flowpowered.nbt.CompoundTag;
import com.infernalsuite.worldmanager.api.source.SlimeWorldSaveSource;
import com.infernalsuite.worldmanager.api.world.property.SlimePropertyMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * In-memory representation of a SRF world.
 */
public interface SlimeWorld {

    /**
     * Returns the name of the world.
     *
     * @return The name of the world.
     */
    String getName();

    /**
     * Returns the {@link SlimeWorldSaveSource} used for saving to this world.
     *
     * @return The {@link SlimeWorldSaveSource} used to save this world, or null if this world is read only.
     */
    @Nullable SlimeWorldSaveSource getSaveSource();

    /**
     * Returns whether or not read-only is enabled.
     *
     * @return true if read-only is enabled, false otherwise.
     */
    default boolean isReadOnly() {
        return getSaveSource() == null;
    }

    /**
     * Returns the chunk that belongs to the coordinates specified.
     *
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return The {@link SlimeChunk} that belongs to those coordinates.
     */
    SlimeChunk getChunk(int x, int z);

    /**
     * Returns a {@link Map} with every {@link SlimeChunk} that is
     * currently loaded in this slime world.
     *
     * @return A {@link Map} containing every loaded chunk.
     */
    Map<Long, SlimeChunk> getChunks();

    /**
     * Returns the extra data of the world. Inside this {@link CompoundTag}
     * can be stored any information to then be retrieved later, as it's
     * saved alongside the world data.
     *
     * @return A {@link CompoundTag} containing the extra data of the world.
     */
    CompoundTag getExtraData();

    /**
     * Returns a {@link Collection} with every world map, serialized
     * in a {@link CompoundTag} object.
     *
     * @return A {@link Collection} containing every world map.
     */
    Collection<CompoundTag> getWorldMaps();

    /**
     * Returns the property map.
     *
     * @return A {@link SlimePropertyMap} object containing all the properties of the world.
     */
    SlimePropertyMap getPropertyMap();

}
