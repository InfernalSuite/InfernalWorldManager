package com.infernalsuite.iwm.api.event.events.sources;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once data source de-registration has been completed.
 *
 * <p>Contains the name of the data source which has been unregistered.</p>
 */
public interface PostUnregisterDataSourceEvent extends IWMEvent {

    /**
     * Gets the name of the data source which has been unregistered.
     *
     * @return the data source name
     */
    @Param(0)
    @NonNull String getDataSourceName();

}
