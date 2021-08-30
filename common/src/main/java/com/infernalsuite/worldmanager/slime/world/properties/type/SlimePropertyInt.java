package com.infernalsuite.worldmanager.slime.world.properties.type;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.Tag;
import com.infernalsuite.worldmanager.slime.world.properties.SlimeProperty;

import java.util.function.Function;

public class SlimePropertyInt extends SlimeProperty<Integer> {

    public SlimePropertyInt(String nbtName, Integer defaultValue) {
        super(nbtName, defaultValue);
    }

    public SlimePropertyInt(String nbtName, Integer defaultValue, Function<Integer, Boolean> validator) {
        super(nbtName, defaultValue, validator);
    }

    @Override
    protected void writeValue(CompoundMap compound, Integer value) {
        compound.put(nbtName, new IntTag(nbtName, value));
    }

    @Override
    protected Integer readValue(Tag<?> compoundTag) {
        return compoundTag.getAsIntTag()
                .map(Tag::getValue)
                .orElse(defaultValue);
    }
}
