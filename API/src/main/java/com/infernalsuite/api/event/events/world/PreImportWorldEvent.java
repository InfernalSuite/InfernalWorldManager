package com.infernalsuite.api.event.events.world;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.Cancellable;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.loaders.IWMLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

/**
 * Called before a world is imported
 *
 * <p>Contains the source world directory, the world name and the {@link IWMLoader loader} being used to import the world.</p>
 *
 * <p>This event can be cancelled using {@link Cancellable#setCancelled(boolean)} to stop the world import process.</p>
 */
public interface PreImportWorldEvent extends IWMEvent, Cancellable {

    /**
     * Gets the world folder containing the world being imported.
     *
     * @return the world folder
     */
    @Param(0)
    @NonNull File getWorldDir();

    /**
     * Gets the name of the world being imported.
     *
     * @return the world name
     */
    @Param(1)
    @NonNull String getWorldName();

    /**
     * Gets the {@link IWMLoader loader} being used during the import process.
     *
     * @return the loader
     */
    @Param(2)
    @NonNull IWMLoader getLoader();

}
