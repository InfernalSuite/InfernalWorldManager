package com.infernalsuite.iwm.api.world.properties.type;

import com.infernalsuite.iwm.api.utils.NBTUtil;
import com.infernalsuite.iwm.api.world.properties.WorldProperty;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTByte;

import java.util.Map;
import java.util.function.Function;

public class BooleanProperty extends WorldProperty<Boolean> {

    public BooleanProperty(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanProperty(String name, Boolean defaultValue, Function<Boolean, Boolean> validator) {
        super(name, defaultValue, validator);
    }

    @Override
    protected void writeValue(Map<String, NBT> map, Boolean value) {
        map.put(getName(), new NBTByte(value));
    }

    @Override
    protected Boolean readValue(NBT tag) {
        return NBTUtil.getAsByteTag(tag)
                .map(NBTByte::asBoolean)
                .orElse(getDefaultValue());
    }

}
