package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.IWMLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once loader registration has been completed.
 *
 * <p>Contains the name of the loader and the {@link IWMLoader} representing the loader.</p>
 */
public interface PostRegisterLoaderEvent extends IWMEvent {

    /**
     * Gets the name of the loader which has been registered.
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

    /**
     * Gets the loader which has been registered.
     *
     * @return the loader
     */
    @Param(1)
    @NonNull IWMLoader getLoader();

}
