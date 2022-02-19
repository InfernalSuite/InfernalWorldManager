package com.infernalsuite.iwm.api.world.properties;

import lombok.Getter;
import org.jglrxavpok.hephaistos.nbt.NBT;

import java.util.Map;
import java.util.function.Function;

@Getter
public abstract class WorldProperty<T> {

    private final String name;
    private final T initialValue;
    private final Function<T, Boolean> validator;

    protected WorldProperty(String name, T initialValue) { this(name, initialValue, null); }

    protected WorldProperty(String name, T initialValue, Function<T, Boolean> validator) {
        this.name = name;
        if (initialValue != null && validator != null && !validator.apply(initialValue)) {
            throw new IllegalArgumentException("Invalid initial value for property: " + name + ", value: " + initialValue);
        }
        this.initialValue = initialValue;
        this.validator = validator;
    }

    protected abstract void writeValue(Map<String, NBT> map, T value);

    protected abstract T readValue(NBT tag);

}
