package com.infernalsuite.iwm.nms;

import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.bukkit.World;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.IOException;

public interface InfernalNMS {

    void setDefaultWorlds(InfernalWorld normalWorld, InfernalWorld netherWorld, InfernalWorld endWorld) throws IOException;

    void generateWorld(InfernalWorld world);

    InfernalWorld getInfernalWorld(World world);

    byte getWorldVersion();

    default NBTCompound convertChunk(NBTCompound chunkTag) { return chunkTag; }

}
