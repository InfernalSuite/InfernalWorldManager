package com.infernalsuite.iwm.common.formats.slime;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

@Getter
@Setter
@RequiredArgsConstructor
public class SlimeSerializedDataWrapper implements SerializedDataWrapper {

    private static final String FORMAT_NAME = "slime";

    // Serialization Metadata (not written to file)
    private final String formatName = FORMAT_NAME;
    private final String worldName;
    private @MonotonicNonNull IWMLoader loader;
    private final boolean readOnly;

    // Format Info
    private int formatBytesLength;
    private byte[] formatBytes;

    // Format Metadata
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
