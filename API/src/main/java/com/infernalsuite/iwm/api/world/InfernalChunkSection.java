package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;

/**
 * A representation of a Chunk Section (16x16x16 cubic) in IWM.
 */
public interface InfernalChunkSection {

    /**
     * Gets the block palette of the chunk section.
     *
     * @apiNote 1.13 - 1.17 Only ({@code null} for post 1.17)
     *
     * @return the block palette, contained inside a {@link NBTList}
     */
    @Nullable NBTList<NBTCompound> getPalette();

    /**
     * Gets all the states of the blocks of the chunk section.
     *
     * @apiNote 1.13 - 1.17 Only ({@code null} for post 1.17)
     *
     * @return a {@code long[]} with every block state
     */
    long[] getBlockStates();

    /**
     * Gets the block states data for this chunk section, in NBT format.
     *
     * @apiNote Post 1.17 Only ({@code null} for pre 1.17)
     *
     * @return the section's block states, contained inside a {@link NBTCompound}
     */
    @Nullable NBTCompound getBlockStatesTag();

    /**
     * Gets the biome data for this chunk section, in NBT format.
     *
     * @apiNote the section's biome data, contained inside a {@link NBTCompound}
     *
     * @return Post 1.17 Only ({@code null} for pre 1.17)
     */
    @Nullable NBTCompound getBiomeTag();

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
