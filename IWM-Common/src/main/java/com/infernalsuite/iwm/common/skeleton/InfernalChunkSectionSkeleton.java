package com.infernalsuite.iwm.common.skeleton;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public record InfernalChunkSectionSkeleton(NBTCompound blockStates, NBTCompound biomeNbt,
                                           NibbleArray blockLightArray, NibbleArray skyLightArray) implements InfernalChunkSection {

    @Override
    public @NonNull NBTCompound getBlockStatesTag() {
        return this.blockStates;
    }

    @Override
    public @NonNull NBTCompound getBiomeTag() {
        return this.biomeNbt;
    }

    @Override
    public @NonNull NibbleArray getBlockLight() {
        return this.blockLightArray;
    }

    @Override
    public @NonNull NibbleArray getSkyLight() {
        return this.skyLightArray;
    }
}
