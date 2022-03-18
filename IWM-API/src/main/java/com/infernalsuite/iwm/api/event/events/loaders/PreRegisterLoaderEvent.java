package com.infernalsuite.iwm.api.event.events.loaders;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
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
