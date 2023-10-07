package com.infernalsuite.iwm.api.world;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

/**
 * A representation of a Chunk Section (16x16x16 cubic) in IWM.
 */
public interface InfernalChunkSection {

    /**
     * Gets the block states data for this chunk section, in NBT format.
     *
     * @return the section's block states, contained inside a {@link NBTCompound}
     */
    @NonNull NBTCompound getBlockStatesTag();

    /**
     * Gets the biome data for this chunk section, in NBT format.
     *
     * @return the section's biome data, contained inside a {@link NBTCompound}
     */
    @NonNull NBTCompound getBiomeTag();

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
