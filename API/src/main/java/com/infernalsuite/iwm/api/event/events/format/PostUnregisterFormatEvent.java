package com.infernalsuite.iwm.api.event.events.format;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once format de-registration has been completed.
 *
 * <p>Contains the name of the format which has been unregistered.</p>
 */
public interface PostUnregisterFormatEvent extends IWMEvent {

    /**
     * Gets the name of the format which has been unregistered.
     *
     * @return the format name
     */
    @Param(0)
    @NonNull String getFormatName();

}
