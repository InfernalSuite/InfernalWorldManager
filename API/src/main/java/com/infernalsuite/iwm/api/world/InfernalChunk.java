package com.infernalsuite.iwm.api.world;

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
    String getWorldName();

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
     * Gets the height maps of the chunk.
     *
     * @return a {@link NBTCompound} containing all the height maps of the chunk
     */
    NBTCompound getHeightMaps();

    /**
     * Gets all the tile entities of the chunk.
     *
     * @return a list of {@link NBTCompound}s containing all the tile entities of the chunk
     */
    List<NBTCompound> getTileEntities();

    /**
     * Gets all entities of the chunk.
     *
     * @return a list of {@link NBTCompound}s containing all the entities of the cxhunk
     */
    List<NBTCompound> getEntities();

}
