package com.infernalsuite.iwm.api.sources;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Registry for available data sources within this IWM instance.
 */
public interface DataSourceRegistry {

    /**
     * Register a {@link DataSource} with IWM.
     *
     * @param dataSource the data source to register
     */
    void register(@NonNull DataSource dataSource);

    /**
     * Gets a {@link DataSource} by its registered name.
     *
     * @param dataSourceName the name of the data source to retrieve
     * @return an {@link Optional} containing the data source, or {@link Optional#empty()} if no registered data source matches the given name
     */
    @NonNull Optional<DataSource> getDataSource(@NonNull String dataSourceName);

    /**
     * Unregister the given {@link DataSource} from the {@link DataSourceRegistry registry}
     *
     * @param dataSource the data source to unregister
     */
    void unregister(@NonNull DataSource dataSource);

    /**
     * Unregister a {@link DataSource} using the data source's registered name.
     *
     * @param dataSourceName the name of the data source to unregister
     */
    void unregister(@NonNull String dataSourceName);

    /**
     * Gets a list of names of the data sources registered with this registry.
     *
     * @return a list of registered data sources names
     */
    @NonNull List<String> getRegisteredDataSources();

}
