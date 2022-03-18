package com.infernalsuite.iwm.common.api.implementation;

import com.infernalsuite.iwm.api.platform.WorldAdapter;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.ApiStatus;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Highly experimental class, using WeakReferencing to provide speedy access to convert between {@link InfernalWorld} and {@link T}
 *
 * <p>This is <strong>very</strong> dependent on GC behaviour, so it should be considered experimental. E.g. A world that's been unloaded by Bukkit
 * will become {@code null} but it won't be removed from the caching maps in this class until the GC collects it!</p>
 * @param <T> the type of platform-specific world objects
 */
@ApiStatus.Experimental
public abstract class IwmWorldAdapter<T> implements WorldAdapter<T> {

    private final Map<InfernalWorld, WeakReference<T>> forwardLookupMap = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<T, WeakReference<InfernalWorld>> reverseLookupMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public @Nullable T getWorldObject(@NonNull InfernalWorld infernalWorld) {
        var wrt = forwardLookupMap.get(infernalWorld);
        return wrt == null ? null : wrt.get();
    }

    @Override
    public boolean isInfernalWorld(@NonNull T worldObject) {
        return reverseLookupMap.containsKey(worldObject);
    }

    @Override
    public @Nullable InfernalWorld getInfernalWorld(@NonNull T worldObject) {
        var wri = reverseLookupMap.get(worldObject);
        return wri == null ? null : wri.get();
    }

    public abstract T getWorldObject0(@NonNull InfernalWorld infernalWorld);

    public void registerGeneratedWorld(InfernalWorld world, T worldObject) {
        WeakReference<InfernalWorld> wri = new WeakReference<>(world);
        WeakReference<T> wrt = new WeakReference<>(worldObject);
        forwardLookupMap.put(world, wrt);
        reverseLookupMap.put(worldObject, wri);
    }

}
