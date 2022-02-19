/*
 * Copyright (c) 2016-2018 Daniel Ennis (Aikar) - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.infernalsuite.iwm.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A local copy of Aikar's Delegating Map - all credits for this go to Daniel Ennis (Aikar) as per the above copyright notice
 * <p>
 * Use permitted under the MIT license, see above - or see: https://opensource.org/licenses/MIT
 * @author Daniel Ennis (Aikar)
 * @version 1.0.0-SNAPSHOT
 */
public interface DelegatingMap<K, V> extends Map<K, V> {

    Map<K, V> delegate(boolean isReadOnly);

    @Override
    default int size() {
        return delegate(true).size();
    }

    @Override
    default boolean isEmpty() {
        return delegate(true).isEmpty();
    }

    @Override
    default boolean containsKey(Object key) {
        return delegate(true).containsKey(key);
    }

    @Override
    default boolean containsValue(Object value) {
        return delegate(true).containsValue(value);
    }

    @Override
    default V get(Object key) {
        return delegate(true).get(key);
    }

    @Nullable
    @Override
    default V put(K key, V value) {
        return delegate(false).put(key, value);
    }

    @Override
    default V remove(Object key) {
        return delegate(false).remove(key);
    }

    @Override
    default void putAll(@NotNull Map<? extends K, ? extends V> m) {
        delegate(false).putAll(m);
    }

    @Override
    default void clear() {
        delegate(false).clear();
    }

    @NotNull
    @Override
    default Set<K> keySet() {
        return delegate(false).keySet();
    }

    @NotNull
    @Override
    default Collection<V> values() {
        return delegate(false).values();
    }

    @NotNull
    @Override
    default Set<Entry<K, V>> entrySet() {
        return delegate(false).entrySet();
    }

    @Override
    default V getOrDefault(Object key, V defaultValue) {
        return delegate(true).getOrDefault(key, defaultValue);
    }

    @Override
    default void forEach(BiConsumer<? super K, ? super V> action) {
        delegate(true).forEach(action);
    }

    @Override
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        delegate(false).replaceAll(function);
    }

    @Nullable
    @Override
    default V putIfAbsent(K key, V value) {
        return delegate(false).putIfAbsent(key, value);
    }

    @Override
    default boolean remove(Object key, Object value) {
        return delegate(false).remove(key, value);
    }

    @Override
    default boolean replace(K key, V oldValue, V newValue) {
        return delegate(false).replace(key, oldValue, newValue);
    }

    @Nullable
    @Override
    default V replace(K key, V value) {
        return delegate(false).replace(key, value);
    }

    @Override
    default V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        return delegate(false).computeIfAbsent(key, mappingFunction);
    }

    @Override
    default V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return delegate(false).computeIfPresent(key, remappingFunction);
    }

    @Override
    default V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return delegate(false).compute(key, remappingFunction);
    }

    @Override
    default V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return delegate(false).merge(key, value, remappingFunction);
    }

}
