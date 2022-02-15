package com.infernalsuite.iwm.api.formats;

import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;

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
    @NonNull InfernalWorld deserialize(@NonNull SerializedDataWrapper serializedWorld);

}
