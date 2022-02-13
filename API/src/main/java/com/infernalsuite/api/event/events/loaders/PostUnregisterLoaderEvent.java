package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once loader de-registration has been completed.
 *
 * <p>Contains the name of the loader which has been unregistered.</p>
 */
public interface PostUnregisterLoaderEvent extends IWMEvent {

    /**
     * Gets the name of the loader which has been unregistered.
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

}
