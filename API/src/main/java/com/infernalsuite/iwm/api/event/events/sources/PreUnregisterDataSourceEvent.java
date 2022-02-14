package com.infernalsuite.iwm.api.event.events.sources;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a data source is unregistered from the {@link DataSourceRegistry data source registry}
 *
 * <p>Contains the name of the data source being unregistered.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the de-registration process.</p>
 */
public interface PreUnregisterDataSourceEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the data source being unregistered.
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

}
