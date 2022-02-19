package com.infernalsuite.iwm.api.formats;

import com.infernalsuite.iwm.api.world.InfernalWorld;

/**
 * A wrapper for the serialized world data stored by IWM
 */
public interface SerializedDataWrapper {

    Byte[] getChunkByteArray(int x, int z);

    void putChunkByteArray(int x, int z, Byte[] serializedChunk);

    /**
     * Gets the name of the format of this serialized data.
     *
     * @apiNote This must match the registered name of a {@link Format} in the {@link FormatRegistry}, as the registered
     * format object is responsible for deserializing & serializing this data into an {@link InfernalWorld}.
     *
     * @return the format name
     */
    String getFormatName();

}
