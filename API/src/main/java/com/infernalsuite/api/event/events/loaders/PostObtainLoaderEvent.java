package com.infernalsuite.api.event.events.loaders;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.ResultEvent;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.IWMLoader;
import com.infernalsuite.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called after a loader has been obtained from the loader registry.
 *
 * <p>Contains the {@link DataSource} which has been used to obtain the loader.</p>
 *
 * <p>Holds the {@link IWMLoader loader} which has been obtained, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostObtainLoaderEvent extends IWMEvent, ResultEvent<IWMLoader> {

    /**
     * Gets the {@link DataSource data source} which has been used to obtain the loader.
     *
     * @return the loader
     */
    @Param(0)
    @NonNull DataSource getDataSource();

    /**
     * Sets the result loader object.
     *
     * @param loader the loader
     */
    default void setLoader(@Nullable IWMLoader loader) {
        result().set(loader);
    }

}
