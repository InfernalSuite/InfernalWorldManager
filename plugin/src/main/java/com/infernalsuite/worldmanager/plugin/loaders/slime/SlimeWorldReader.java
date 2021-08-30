package com.infernalsuite.worldmanager.plugin.loaders.slime;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.nms.CraftSlimeWorld;

import java.io.DataInputStream;
import java.io.IOException;

public interface SlimeWorldReader {

    CraftSlimeWorld deserializeWorld(byte version, SlimeLoader loader, String worldName, DataInputStream dataStream, SlimePropertyMap propertyMap, boolean readOnly) throws IOException, CorruptedWorldException, NewerFormatException;
}
