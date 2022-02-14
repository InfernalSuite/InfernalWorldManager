package com.infernalsuite.iwm.api.event.events.sources;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a {@link DataSource} is registered with the {@link DataSourceRegistry}
 *
 * <p>Contains the name of the data source being registered and the {@link DataSource} representing the data source.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the registration process.</p>
 */
public interface PreRegisterDataSourceEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the data source to be registered.
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

    /**
     * Gets the {@link DataSource} to be registered.
     *
     * @return the data source
     */
    @Param(1)
    @NonNull DataSource getDataSource();

}
