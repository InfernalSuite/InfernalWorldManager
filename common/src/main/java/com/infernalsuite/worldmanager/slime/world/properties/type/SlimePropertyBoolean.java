package com.infernalsuite.worldmanager.slime.world.properties.type;

import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.Tag;
import com.infernalsuite.worldmanager.slime.world.properties.SlimeProperty;

import java.util.function.Function;

public class SlimePropertyBoolean extends SlimeProperty<Boolean> {

	public SlimePropertyBoolean(String nbtName, Boolean defaultValue) {
		super(nbtName, defaultValue);
	}

	public SlimePropertyBoolean(String nbtName, Boolean defaultValue, Function<Boolean, Boolean> validator) {
		super(nbtName, defaultValue, validator);
	}

	@Override
	protected void writeValue(CompoundMap compound, Boolean value) {
		compound.put(nbtName, new ByteTag(nbtName, (byte) (value ? 1 : 0)));
	}

	@Override
	protected Boolean readValue(Tag<?> compoundTag) {
		return compoundTag.getAsByteTag()
			.map((value) -> value.getValue() == 1)
			.orElse(defaultValue);
	}
}
