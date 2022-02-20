package com.infernalsuite.iwm.api.formats;

import com.infernalsuite.iwm.api.world.InfernalWorld;

/**
 * A wrapper for the serialized world data stored by IWM
 */
public interface SerializedDataWrapper {

    /**
     * Gets the name of the format of this serialized data.
     *
     * @apiNote This must match the registered name of a {@link Format} in the {@link FormatRegistry}, as the registered
     * format object is responsible for deserializing & serializing this data into an {@link InfernalWorld}.
     *
     * @return the format name
     */
    String getFormatName();

    /**
     * Gets the name of the world which this serialized data represents.
     *
     * @return the world name
     */
    String getWorldName();

}
