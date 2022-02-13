package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.LoaderRegistry;
import com.infernalsuite.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a loader is obtained from the {@link LoaderRegistry loader registry}
 *
 * <p>Contains the {@link DataSource} being used to obtain the loader.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} which will cause the
 * {@link LoaderRegistry#obtainLoader(DataSource)} call to return {@code null}</p>
 */
public interface PreObtainLoaderEvent extends IWMEvent, Cancellable {

    /**
     * Gets the {@link DataSource data source} being used to obtain a loader.
     *
     * @return the data source
     */
    @Param(0)
    @NonNull DataSource getDataSource();

}
