package com.infernalsuite.api.event.events.world;

import com.infernalsuite.api.event.IWMEvent;
import com.infernalsuite.api.event.type.ResultEvent;
import com.infernalsuite.api.event.util.Param;
import com.infernalsuite.api.world.InfernalWorld;
import com.infernalsuite.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called after a world has been retrieved from the world registry.
 *
 * <p>Contains the name of the world which has been retrieved.</p>
 *
 * <p>Holds the {@link InfernalWorld world} which has been obtained, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostGetWorldEvent extends IWMEvent, ResultEvent<InfernalWorld> {

    /**
     * Get the name of the world which has been retrieved from the {@link WorldRegistry world registry}
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Sets the result Infernal World object
     *
     * @param world the world
     */
    default void setInfernalWorld(@Nullable InfernalWorld world) {
        result().set(world);
    }

}
