package com.infernalsuite.iwm.api.event.events.format;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called after a format has been retrieved from the format registry.
 *
 * <p>Contains the name of the format which has been registered.</p>
 *
 * <p>Holds the {@link Format} which has been obtained, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostGetFormatEvent extends IWMEvent, ResultEvent<Format> {

    /**
     * Gets the name of the format which has been retrieved from the {@link FormatRegistry format registry}
     *
     * @return the format name
     */
    @Param(0)
    @NonNull String getFormatName();

    /**
     * Sets the result format object.
     *
     * @param format the format
     */
    default void setFormat(@Nullable Format format) {
        result().set(format);
    }

}
