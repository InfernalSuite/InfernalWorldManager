package com.infernalsuite.iwm.api.loaders;

import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.List;

/**
 * Represents a loader that can load worlds from a data source.
 */
public interface IWMLoader {

    /**
     * Get the data source used by this loader
     * @return the data source
     */
    @NonNull DataSource getDataSource();

    /**
     * Set the data source to be used by this loader
     * @param dataSource the data source to use
     */
    void setDataSource(@NonNull DataSource dataSource);

    /**
     * Get the name of this loader
     * @return the name of the loader
     */
    @NonNull String getName();

    /**
     * Loads the serialized data for the given world from this loader.
     *
     * @param worldName the name of the world to load
     * @param readOnly whether the world is read only
     * @return the serialized world data
     */
    @NonNull SerializedDataWrapper loadWorld(@NonNull String worldName, boolean readOnly);

    /**
     * Checks if a world is present in this loader.
     *
     * @param worldName the world name to check
     * @return {@code true} if the world is stored in this loader
     */
    boolean worldExists(@NonNull String worldName);

    /**
     * Gets a list of names of world stored in this loader.
     *
     * @return a list of names of stored worlds
     */
    @NonNull List<String> listWorlds();

    /**
     * Saves a world using this loader.
     *
     * @param worldName the name of the world to save
     * @param world the serialized data for this world
     * @param lock whether the world should be locked
     */
    void saveWorld(@NonNull String worldName, @NonNull SerializedDataWrapper world, boolean lock);

    /**
     * Unlocks a world.
     *
     * @param worldName the world to unlock
     */
    void unlockWorld(@NonNull String worldName) throws IOException;

    /**
     * Checks whether a world is locked.
     *
     * @param worldName the name of the world to check
     * @return {@code true} if the world is locked
     */
    boolean isWorldLocked(@NonNull String worldName) throws IOException;

    /**
     * Deletes a world.
     *
     * @param worldName the name of the world to delete
     */
    void deleteWorld(@NonNull String worldName);

}
