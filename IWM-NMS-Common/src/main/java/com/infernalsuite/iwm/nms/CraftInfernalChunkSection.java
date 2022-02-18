package com.infernalsuite.iwm.nms;

import com.infernalsuite.iwm.api.utils.NibbleArray;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;

@Getter
@AllArgsConstructor
public class CraftInfernalChunkSection implements InfernalChunkSection {

    //region 1.13 - 1.17
    //=========================================================================

    private final NBTList<NBTCompound> palette;
    private final long[] blockStates;

    //=========================================================================
    //endregion

    //region 1.18+
    //=========================================================================

    @Setter
    private NBTCompound blockStatesTag;
    @Setter
    private NBTCompound biomeTag;

    //=========================================================================
    //endregion

    //region All
    //=========================================================================

    private final NibbleArray blockLight;
    private final NibbleArray skyLight;

    //=========================================================================
    //endregion

}
