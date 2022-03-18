package com.infernalsuite.iwm.api.world.properties;

import lombok.RequiredArgsConstructor;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of properties related to a world
 */
@RequiredArgsConstructor
public class WorldPropertyMap {

    private final Map<String, NBT> properties;

    public WorldPropertyMap() { this(new HashMap<>()); }

    public <T> T getValue(WorldProperty<T> property) {
        if (properties.containsKey(property.getName())) {
            return property.readValue(properties.get(property.getName()));
        } else {
            return property.getDefaultValue();
        }
    }

    public <T> void setValue(WorldProperty<T> property, T value) {
        if (property.getValidator() != null && !property.getValidator().apply(value)) {
            throw new IllegalArgumentException("'" + value + "' is not a valid property value");
        }
        property.writeValue(properties, value);
    }

    public void merge(WorldPropertyMap propertyMap) {
        this.properties.putAll(propertyMap.properties);
    }

    public NBTCompound toCompound() { return new NBTCompound(properties); }

}
