package com.infernalsuite.iwm.api.event.events.world;

import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Called once world creation has been completed.
 *
 * <p>Contains the name of the world which has been created.</p>
 *
 * <p>Holds the world which has been created, retrievable with {@link ResultEvent#getResult()}</p>
 */
public interface PostCreateEmptyWorldEvent extends IWMEvent, ResultEvent<InfernalWorld> {

    /**
     * Gets the name of the world which has been created.
     *
     * @return the world name
     */
    @Param(0)
    @NonNull String getWorldName();

    /**
     * Sets the result Infernal World object.
     *
     * @param world the world
     */
    default void setInfernalWorld(@Nullable InfernalWorld world) {
        result().set(world);
    }

}
