package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called before a world is migrated to a new loader/new data source.
 *
 * <p>Contains the world name, the current loader and the new loader.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the migration process.</p>
 */
public interface PreMigrateWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the name of the world being migrated.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Gets the current loader to which the world belongs.
     *
     * @return the current loader
     */
    @Param(1)
    @NonNull IWMLoader getCurrentLoader();

    /**
     * Gets the new loader the world is being migrated to.
     *
     * @return the new loader
     */
    @Param(2)
    @NonNull IWMLoader getNewLoader();

}
