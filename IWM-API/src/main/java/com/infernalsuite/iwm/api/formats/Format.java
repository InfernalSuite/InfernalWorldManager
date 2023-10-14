package com.infernalsuite.iwm.api.formats;

import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a world format within IWM
 */
public interface Format {

    /**
     * Gets the unique name of this format.
     *
     * @return the format name
     */
    @NonNull String getName();

    /**
     * Serialize a {@link InfernalWorld} into a {@link SerializedDataWrapper} in this format.
     *
     * @param world the world to serialize
     * @return the serialized world data
     */
    @NonNull SerializedDataWrapper serialize(@NonNull InfernalWorld world);

    /**
     * Deserialize a {@link SerializedDataWrapper} into a {@link InfernalWorld} in this format.
     *
     * @param serializedWorld the world to deserialize
     * @return the {@link InfernalWorld deserialized world}
     */
    @NonNull InfernalWorld deserialize(@NonNull SerializedDataWrapper serializedWorld) throws IOException;

    /**
     * Serializes an {@link InfernalChunk} into a byte array in this format.
     *
     * @param chunk the chunk to serialize
     * @return the serialized chunk as a byte array
     */
    byte[] serializeChunk(@NonNull InfernalChunk chunk);

    /**
     * Deserializes a byte array into an {@link InfernalChunk} in this format.
     *
     * @param serialized the byte array to deserialize
     * @return the {@link InfernalChunk deserialized chunk}
     */
    InfernalChunk deserializeChunk(byte[] serialized);

    /**
     * Constructs a {@link SerializedDataWrapper} from the given Input Stream.
     * @param worldName the name of the world
     * @param readOnly {@code true} if the world is read only
     * @param inputStream the Input Stream to construct the Serialized Data Wrapper from
     * @return The resulting Serialized Data Wrapper, containing the compressed data for the world
     */
    @NonNull SerializedDataWrapper readDataWrapper(@NonNull String worldName, boolean readOnly, @NonNull InputStream inputStream);

    /**
     * Writes the given {@link SerializedDataWrapper} to an OutputStream.
     *
     * @param serializedDataWrapper the Serialized Data Wrapper to write
     * @param outputStream the Output Stream to write to
     */
    void writeDataWrapper(@NonNull SerializedDataWrapper serializedDataWrapper, @NonNull OutputStream outputStream);

}
