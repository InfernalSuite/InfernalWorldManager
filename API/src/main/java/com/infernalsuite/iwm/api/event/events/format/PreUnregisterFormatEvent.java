package com.infernalsuite.iwm.api.event.events.format;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a format is unregistered from the {@link FormatRegistry}
 *
 * <p>Contains the name of the format being unregistered.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the de-registration process.</p>
 */
public interface PreUnregisterFormatEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the format being unregistered.
     *
     * @return the format name
     */
    @Param(0)
    @NonNull String getFormatName();

}
