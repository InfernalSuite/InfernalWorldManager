package com.infernalsuite.iwm.api.event.events.format;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.formats.Format;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once format registration has been completed.
 *
 * <p>Contains the name of the format and the {@link Format} representing the format.</p>
 */
public interface PostRegisterFormatEvent extends IWMEvent {

    /**
     * Gets the name of the format which has been registered.
     *
     * @return the format name
     */
    @Param(0)
    @NonNull String getFormatName();

    /**
     * Gets the format which has been registered.
     *
     * @return the format
     */
    @Param(1)
    @NonNull Format getFormat();

}
