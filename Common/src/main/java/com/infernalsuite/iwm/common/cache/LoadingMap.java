package com.infernalsuite.iwm.common.cache;

import com.google.common.collect.ForwardingMap;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A map which will either retrieve the stored value, or insert a new value into the map using the given function
 */
@RequiredArgsConstructor
public class LoadingMap<K, V> extends ForwardingMap<K, V> implements Map<K, V> {

    public static <K, V> @NonNull LoadingMap<K, V> of(@NonNull Map<K, V> map, @NonNull Function<K, V> function) {
        return new LoadingMap<>(map, function);
    }

    public static <K, V> @NonNull LoadingMap<K, V> of(@NonNull Function<K, V> function) {
        return of(new ConcurrentHashMap<>(), function);
    }

    private final @NonNull Map<K, V> map;
    private final @NonNull Function<K, V> function;

    @Override
    protected @NonNull Map<K, V> delegate() {
        return this.map;
    }

    public @Nullable V getIfPresent(@Nullable K key) { return this.map.get(key); }

    @Override
    public @Nullable V get(@Nullable Object key) {
        V value = this.map.get(key);
        if (value != null) {
            return value;
        }
        //noinspection unchecked
        return this.map.computeIfAbsent((K) key, this.function);
    }
}
