package com.infernalsuite.iwm.api.formats;

/**
 * A wrapper for the serialized world data stored by IWM
 */
public interface SerializedDataWrapper {

    byte[] getChunkByteArray(int x, int z);

    void putChunkByteArray(int x, int z, byte[] serializedChunk);

}
