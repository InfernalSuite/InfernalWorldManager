package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once world migration has been completed.
 *
 * <p>Contains the world name, old loader and new loader.</p>
 */
public interface PostMigrateWorldEvent extends IWMEvent {

    /**
     * Gets the name of the world which has been migrated.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Gets the old loader which the world used to belong to.
     *
     * @return the old loader
     */
    @Param(1)
    @NonNull IWMLoader getOldLoader();

    /**
     * Gets the new loader to which the world now belongs.
     *
     * @return the new loader
     */
    @Param(2)
    @NonNull IWMLoader getNewLoader();

}
