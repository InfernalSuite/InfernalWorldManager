package com.infernalsuite.api.loaders;

import com.infernalsuite.api.sources.DataSource;
import com.infernalsuite.api.world.InfernalWorld;
import com.infernalsuite.api.world.WorldDataWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    @NonNull WorldDataWrapper loadWorld(@NonNull String worldName, boolean readOnly);

    boolean worldExists(@NonNull String worldName);

    List<String> listWorlds();

    void saveWorld(@NonNull String worldName, @NonNull WorldDataWrapper world, boolean lock);

    void unlockWorld(@NonNull String worldName);

    boolean isWorldLocked(@NonNull String worldName);

    void deleteWorld(@NonNull String worldName);

}
