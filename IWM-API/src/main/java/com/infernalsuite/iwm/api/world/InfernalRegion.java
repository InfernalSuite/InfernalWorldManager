package com.infernalsuite.iwm.api.world;

/**
 * A representation of a region (32x32 chunks) within IWM.
 */
public interface InfernalRegion {

    /**
     * Gets the location table for this region.
     *
     * @return the location table
     */
    int[] getLocationTable();

    /**
     * Gets the timestamp table for this region.
     *
     * @return the timestamp table
     */
    int[] getTimestampTable();

    /**
     * Gets a chunk at the given co-ordinates.
     *
     * @param cx the chunk x co-ordinate
     * @param cz the chunk z co-ordinate
     * @return the {@link InfernalChunk} at the given position
     */
    InfernalChunk getChunk(int cx, int cz);

    /**
     * Utility method for calculating the table offset for a given chunk.
     *
     * @param cx the chunk x co-ordinate
     * @param cz the chunk y co-ordinate
     * @return the table offset for the chunk's data
     */
    static int getHeaderOffset(int cx, int cz) {
        return ((cx % 32) + (cz % 32) * 32) * 4;
    }

}
