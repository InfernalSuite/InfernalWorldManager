package com.infernalsuite.iwm.api.world.properties;

import lombok.Getter;
import org.jglrxavpok.hephaistos.nbt.NBT;

import java.util.Map;
import java.util.function.Function;

@Getter
public abstract class WorldProperty<T> {

    private final String name;
    private final T defaultValue;
    private final Function<T, Boolean> validator;

    protected WorldProperty(String name, T defaultValue) { this(name, defaultValue, null); }

    protected WorldProperty(String name, T defaultValue, Function<T, Boolean> validator) {
        this.name = name;
        if (defaultValue != null && validator != null && !validator.apply(defaultValue)) {
            throw new IllegalArgumentException("Invalid initial value for property: " + name + ", value: " + defaultValue);
        }
        this.defaultValue = defaultValue;
        this.validator = validator;
    }

    protected abstract void writeValue(Map<String, NBT> map, T value);

    protected abstract T readValue(NBT tag);

}
