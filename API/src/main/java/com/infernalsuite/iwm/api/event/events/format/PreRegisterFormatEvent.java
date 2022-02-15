package com.infernalsuite.iwm.api.event.events.format;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a format is registered with the {@link FormatRegistry}
 *
 * <p>Contains the name of the format and the {@link Format} representing the format.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the format registration process.</p>
 */
public interface PreRegisterFormatEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the format to be registered.
     *
     * @return the format name
     */
    @Param(0)
    @NonNull String getFormatName();

    /**
     * Gets the {@link Format} to be registered.
     *
     * @return the format
     */
    @Param(1)
    @NonNull Format getFormat();

}
