package com.infernalsuite.worldmanager.slime.world.properties;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.Tag;

import java.util.function.Function;

/**
 * A property describing behavior of a slime world.
 */
public abstract class SlimeProperty<T> {

    protected final String nbtName;
    protected final T defaultValue;
    protected final Function<T, Boolean> validator;

    protected SlimeProperty(String nbtName, T defaultValue) {
        this(nbtName, defaultValue, null);
    }

    protected SlimeProperty(String nbtName, T defaultValue, Function<T, Boolean> validator) {
        this.nbtName = nbtName;

        if (defaultValue != null && validator != null && !validator.apply(defaultValue)) {
            throw new IllegalArgumentException("Invalid default value for property " + nbtName + "! " + defaultValue);
        }

        this.defaultValue = defaultValue;
        this.validator = validator;
    }

    protected abstract void writeValue(CompoundMap compound, T value);

    protected abstract T readValue(Tag<?> compoundTag);

    @Override
    public String toString() {
        return "SlimeProperty{" +
            "nbtName='" + nbtName + '\'' +
            ", defaultValue=" + defaultValue +
            '}';
    }

    public String getNbtName() {
        return nbtName;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
