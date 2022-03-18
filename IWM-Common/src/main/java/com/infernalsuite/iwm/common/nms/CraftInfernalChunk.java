package com.infernalsuite.iwm.common.nms;

import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.List;

@Getter
@AllArgsConstructor
public class CraftInfernalChunk implements InfernalChunk {

    private final String worldName;
    private final int x;
    private final int z;

    @Setter
    private InfernalChunkSection[] sections;
    private final NBTCompound heightMaps;
    private final int[] biomes;
    private final List<NBTCompound> tileEntities;
    private final List<NBTCompound> entities;

    @Setter
    private int minSection;
    @Setter
    private int maxSection;

}
