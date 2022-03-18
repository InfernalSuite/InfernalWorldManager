package com.infernalsuite.iwm.api.sources;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Parent data source interface for IWM data sources
 */
public interface DataSource {

    /**
     * Returns the unique name of this data source.
     *
     * @return the data source name
     */
    @NonNull String getName();

    /**
     * Gets whether this data source is enabled.
     *
     * @apiNote If {@code true}, an attempt will be made to create a loader for this data source on startup
     *
     * @return {@code true} if the data source is enabled
     */
    boolean isEnabled();

}
