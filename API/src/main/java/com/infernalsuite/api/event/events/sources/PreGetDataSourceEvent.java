package com.infernalsuite.api.event.events.sources;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.sources.DataSourceRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a data source is retrieved from the {@link DataSourceRegistry data source registry}
 *
 * <p>Contains the name of the data source being retrieved.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} which will cause the
 * {@link DataSourceRegistry#getDataSource(String)} call to return {@code null}</p>
 */
public interface PreGetDataSourceEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the data source being retrieved from the {@link DataSourceRegistry data source registry}
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

}
