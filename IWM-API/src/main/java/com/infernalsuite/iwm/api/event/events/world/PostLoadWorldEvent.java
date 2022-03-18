package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called once world loading has been completed.
 *
 * <p>Contains the name of the world which has been loaded.</p>
 *
 * <p>Holds the {@link InfernalWorld world} which has been loaded, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostLoadWorldEvent extends IWMEvent, ResultEvent<InfernalWorld> {

    /**
     * Gets the name of the world which has been loaded.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Sets the result Infernal World object
     * @param world the world
     */
    default void setInfernalWorld(@Nullable InfernalWorld world) {
        result().set(world);
    }

}
