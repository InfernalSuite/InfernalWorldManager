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

}
