package com.infernalsuite.iwm.common.formats.slime;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SlimeSerializedDataWrapper implements SerializedDataWrapper {

    private final String formatName;

    // Format Info
    private int formatBytesLength;
    private byte[] formatBytes;

    // Metadata
    private byte[] slimeHeader;
    private byte slimeVersion;
    private byte worldVersion;

    // World Info
    private int minX;
    private int minZ;
    private int width;
    private int depth;

    // Chunk Data
    private byte[] chunkBitmask;
    private int compressedChunkBytesLength;
    private int chunkBytesLength;
    private byte[] chunkBytes;

    // Tile Entities
    private int compressedTileEntitiesLength;
    private int tileEntitiesLength;
    private byte[] tileEntities;

    // Entities
    private boolean hasEntities;
    private int compressedEntitiesLength;
    private int entitiesLength;
    private byte[] entities;

    // Extra NBT Data
    private int compressedNbtLength;
    private int nbtLength;
    private byte[] nbtBytes;

    // World Maps NBT Data
    private int compressedMapsTagLength;
    private int mapsTagLength;
    private byte[] mapsTag;

}
