package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Called once world registration has been completed.
 *
 * <p>Contains the name of the registered world and the respective {@link InfernalWorld world}</p>
 */
public interface PostRegisterWorldEvent extends IWMEvent {

    /**
     * Gets the name of the world which has been registered.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Gets the {@link InfernalWorld world} which has been registered.
     *
     * @return the world
     */
    @Param(1)
    @NonNull InfernalWorld getWorld();

}
