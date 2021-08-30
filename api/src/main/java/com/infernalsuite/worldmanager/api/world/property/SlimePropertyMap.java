package com.infernalsuite.worldmanager.api.world.property;

import java.util.Set;

/**
 * A Property Map object.
 */
public interface SlimePropertyMap {

    /**
     * Return the current value of the given property
     *
     * @param property The slime property
     * @return The current value
     */
    <T> T getValue(SlimeProperty<T> property);

    /**
     * Update the value of the given property
     *
     * @param property The slime property
     * @param value    The new value
     * @throws IllegalArgumentException if the value fails validation.
     */
    <T> void setValue(SlimeProperty<T> property, T value);

    /**
     * Copies all values from the specified {@link SlimePropertyMap}.
     * If the same property has different values on both maps, the one
     * on the providen map will be used.
     *
     * @param propertyMap A {@link SlimePropertyMap}.
     */
    void merge(SlimePropertyMap propertyMap);

    /**
     * Returns a list of immutable keys of all properties in this map
     *
     * @return immutable set of keys
     */
    Set<SlimePropertyIdentifier<?>> getKeys();

}
