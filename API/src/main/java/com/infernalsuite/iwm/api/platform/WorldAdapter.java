package com.infernalsuite.iwm.api.platform;

import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Adapts platform World objects to IWM {@link InfernalWorld}s and vice versa.
 *
 * <p>The "world type" parameter must be equal to the class or interface used by the platform to represent worlds.</p>
 *
 * @param <T> The world type
 */
public interface WorldAdapter<T> {

    /**
     * Gets the platform's world object for the given {@link InfernalWorld}
     *
     * @param infernalWorld the infernal world
     * @return the platform's world object, or {@code null} if the {@link InfernalWorld} is not loaded into the platform
     */
    @Nullable T getWorldObject(@NonNull InfernalWorld infernalWorld);

    /**
     * Checks if the given platform world object is an infernal world
     * @param worldObject the world object to check with the World Registry
     * @return {@code true} if the given world object is an {@link InfernalWorld}, otherwise {@code false}
     */
    boolean isInfernalWorld(@NonNull T worldObject);

    /**
     * Get the {@link InfernalWorld} object that corresponds to the given world object
     * @param worldObject the platform's world object
     * @return the {@link InfernalWorld} that represents the given world object, or {@code null} if the given world object isn't an InfernalWorld
     * @apiNote This method can return {@code null} if the world object does not represent an {@link InfernalWorld}. It is recommended to call
     * {@link #isInfernalWorld(Object) isInfernalWorld} - if that method returns {@code true}, this method will never return {@code null} for the same world object.
     */
    @Nullable InfernalWorld getInfernalWorld(@NonNull T worldObject);

}
