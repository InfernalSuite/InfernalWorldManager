package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.LoaderRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a loader is retrieved from the {@link LoaderRegistry loader registry}
 *
 * <p>Contains the name of the loader being retrieved.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} which will cause
 * the {@link LoaderRegistry#getLoader(String)} call to return {@code null}</p>
 */
public interface PreGetLoaderEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the loader being retrieved from the {@link LoaderRegistry loader registry}
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

}

