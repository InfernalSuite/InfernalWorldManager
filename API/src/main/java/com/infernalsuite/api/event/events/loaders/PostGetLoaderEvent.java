package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.ResultEvent;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.IWMLoader;
import com.infernalsuite.api.loaders.LoaderRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called after a loader has been retrieved from the loader registry.
 *
 * <p>Contains the name of the loader which has been retrieved.</p>
 *
 * <p>Holds the {@link IWMLoader loader} which has been obtained, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostGetLoaderEvent extends IWMEvent, ResultEvent<IWMLoader> {

    /**
     * Gets the name of the loader which has been retrieved from the {@link LoaderRegistry loader registry}
     *
     * @return the loader name
     */
    @Param(0)
    @NonNull String getLoaderName();

    /**
     * Sets the result loader object
     *
     * @param loader the loader
     */
    default void setLoader(@Nullable IWMLoader loader) {
        result().set(loader);
    }

}
