package com.infernalsuite.iwm.common.nms;

import com.infernalsuite.iwm.api.world.InfernalWorld;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.IOException;

public interface InfernalNMS<T> {

    Object injectDefaultWorlds();

    void setDefaultWorlds(InfernalWorld normalWorld, InfernalWorld netherWorld, InfernalWorld endWorld) throws IOException;

    void generateWorld(InfernalWorld world);

    InfernalWorld getInfernalWorld(T world);

    byte getWorldVersion();

}
