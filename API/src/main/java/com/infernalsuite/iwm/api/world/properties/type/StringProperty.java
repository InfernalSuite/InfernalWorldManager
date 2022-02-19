package com.infernalsuite.iwm.api.world.properties.type;

import com.infernalsuite.iwm.api.utils.NBTUtil;
import com.infernalsuite.iwm.api.world.properties.WorldProperty;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTString;

import java.util.Map;
import java.util.function.Function;

public class StringProperty extends WorldProperty<String> {

    public StringProperty(String name, String defaultValue) {
        super(name, defaultValue);
    }

    public StringProperty(String name, String defaultValue, Function<String, Boolean> validator) {
        super(name, defaultValue, validator);
    }

    @Override
    protected void writeValue(Map<String, NBT> map, String value) {
        map.put(getName(), new NBTString(value));
    }

    @Override
    protected String readValue(NBT tag) {
        return NBTUtil.getAsStringTag(tag)
                .map(NBTString::getValue)
                .orElse(getDefaultValue());
    }

}
