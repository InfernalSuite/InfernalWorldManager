package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.IWMLoader;
import com.infernalsuite.api.loaders.LoaderRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a loader is registered with the {@link LoaderRegistry}
 *
 * <p>Contains the name of the loader being registered and the {@link IWMLoader} representing the loader.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the loader registration process.</p>
 */
public interface PreRegisterLoaderEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the loader to be registered.
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

    /**
     * Gets the {@link IWMLoader} to be registered.
     *
     * @return the loader
     */
    @Param(1)
    @NonNull IWMLoader getLoader();

}
