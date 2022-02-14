package com.infernalsuite.iwm.api.event.events.loaders;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a loader is unregistered from the {@link LoaderRegistry loader registry}
 *
 * <p>Contains the name of the loader being unregistered.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the de-registration process.</p>
 */
public interface PreUnregisterLoaderEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the loader being unregistered.
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

}
