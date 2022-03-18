package com.infernalsuite.iwm.api.world;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.List;

/**
 * A representation of a Chunk Column (16x16 blocks) in IWM.
 */
public interface InfernalChunk {

    /**
     * Gets the name of the world this chunk belongs to.
     *
     * @return the world name
     */
    @NonNull String getWorldName();

    /**
     * Gets the X co-ordinate of the chunk.
     *
     * @return the X co-ordinate
     */
    int getX();

    /**
     * Gets the Z co-ordinate of the chunk.
     *
     * @return the Z co-ordinate
     */
    int getZ();

    /**
     * Returns all the sections of the chunk.
     *
     * @return a {@link InfernalChunkSection} array
     */
    InfernalChunkSection[] getSections();

    /**
     * Gets the Y-Section co-ordinate of the minimum chunk section in this column.
     *
     * @return the minimum y-section co-ordinate
     */
    int getMinSection();

    /**
     * Gets the Y-Section co-ordinate of the maximum chunk section in this column.
     *
     * @return the maximum y-section co-ordinate
     */
    int getMaxSection();

    /**
     * Gets all the biomes of the chunk.
     *
     * @apiNote Pre 1.17 Only ({@code null} for post 1.17)
     *
     * @return an {@code int[]} containing all the biomes of the chunk
     */
    int[] getBiomes();

    /**
     * Gets the height maps of the chunk.
     *
     * @return a {@link NBTCompound} containing all the height maps of the chunk
     */
    @NonNull NBTCompound getHeightMaps();

    /**
     * Gets all the tile entities of the chunk.
     *
     * @return a list of {@link NBTCompound}s containing all the tile entities of the chunk
     */
    @NonNull List<NBTCompound> getTileEntities();

    /**
     * Gets all entities of the chunk.
     *
     * @return a list of {@link NBTCompound}s containing all the entities of the cxhunk
     */
    @NonNull List<NBTCompound> getEntities();

}
