package com.infernalsuite.iwm.api.event.events.sources;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called after a data source has been retrieved from the data source registry.
 *
 * <p>Contains the name of the data source which has been retrieved.</p>
 *
 * <p>Holds the {@link DataSource data source} which has been obtained, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostGetDataSourceEvent extends IWMEvent, ResultEvent<DataSource> {

    /**
     * Gets the name of the data source which has been retrieved from the {@link DataSourceRegistry data source registry}
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

    /**
     * Sets the result data source object.
     *
     * @param dataSource the data source
     */
    default void setDataSource(@Nullable DataSource dataSource) {
        result().set(dataSource);
    }
}
