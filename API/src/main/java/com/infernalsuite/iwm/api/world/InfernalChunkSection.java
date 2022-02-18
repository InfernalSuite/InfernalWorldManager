package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;

/**
 * A representation of a Chunk Section (16x16x16 cubic) in IWM.
 */
public interface InfernalChunkSection {

    /**
     * Gets the number of non-air blocks present in the chunk section.
     *
     * @return a count of the number of non-air blocks in this chunk section
     */
    short getBlockCount();

    /**
     * Gets all the states of the blocks of the chunk section.
     *
     * @return a {@code long[]} with every block state
     */
    long[] getBlockStates();

    /**
     * Gets the block palette of the chunk section.
     *
     * @return the block palette, contained inside a {@link NBTList}
     */
    @NonNull NBTList<NBTCompound> getPalette();

    /**
     * Gets the biome palette of the chunk section.
     *
     * @return the biome palette, contained inside a {@link NBTList}
     */
    @NonNull NBTList<NBTCompound> getBiomes();

    /**
     * Gets the block light data for this chunk section.
     *
     * @return a {@link NibbleArray} with the block light data
     */
    @NonNull NibbleArray getBlockLight();

    /**
     * Gets the sky light data for this chunk section.
     *
     * @return a {@link NibbleArray} with the sky light data.
     */
    @NonNull NibbleArray getSkyLight();

}
