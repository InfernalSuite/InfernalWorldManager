package com.infernalsuite.iwm.api.event.events.sources;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once data source registration has been completed.
 *
 * <p>Contains the name of the data source and the {@link DataSource} representing the data source.</p>
 */
public interface PostRegisterDataSourceEvent extends IWMEvent {

    /**
     * Gets the name of the data source which has been registered.
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

    /**
     * Gets the data source which has been registered.
     *
     * @return the data source
     */
    @Param(1)
    @NonNull DataSource getDataSource();
}
