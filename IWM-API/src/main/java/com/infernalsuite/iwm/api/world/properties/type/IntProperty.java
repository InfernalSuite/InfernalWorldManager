package com.infernalsuite.iwm.api.world.properties.type;

import com.infernalsuite.iwm.api.utils.NBTUtil;
import com.infernalsuite.iwm.api.world.properties.WorldProperty;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTInt;

import java.util.Map;
import java.util.function.Function;

public class IntProperty extends WorldProperty<Integer> {

    public IntProperty(String name, Integer defaultValue) {
        super(name, defaultValue);
    }

    public IntProperty(String name, Integer defaultValue, Function<Integer, Boolean> validator) {
        super(name, defaultValue, validator);
    }

    @Override
    protected void writeValue(Map<String, NBT> map, Integer value) {
        map.put(getName(), new NBTInt(value));
    }

    @Override
    protected Integer readValue(NBT tag) {
        return NBTUtil.getAsIntTag(tag)
                .map(NBTInt::getValue)
                .orElse(getDefaultValue());
    }

}
