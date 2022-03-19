package com.infernalsuite.iwm.legacy;

import com.infernalsuite.iwm.common.formats.slime.SlimeSerializedDataWrapper;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SlimeReader {

    public static final byte[] SLIME_HEADER = new byte[] { -79, 11 }; // Slime Header
    public static final byte SLIME_VERSION = 9; // Latest pure slime version that this class can read in

    public static SlimeSerializedDataWrapper readSlimeWorld(String worldName) {

        SlimeSerializedDataWrapper serializedDataWrapper = new SlimeSerializedDataWrapper(
                worldName,
                null,
                true
        );

        try {

            RandomAccessFile file = new RandomAccessFile(new File("slime_worlds", worldName + ".slime"), "rw");
            byte[] serializedWorld = new byte[(int) file.length()];
            file.seek(0);
            file.readFully(serializedWorld);
            DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(serializedWorld));

            byte[] formatBytes = "slime".getBytes(StandardCharsets.UTF_8);
            serializedDataWrapper.setFormatBytes(formatBytes);
            serializedDataWrapper.setFormatBytesLength(formatBytes.length);

            byte[] slimeHeader = new byte[SLIME_HEADER.length];
            dataStream.read(slimeHeader);

            if (!Arrays.equals(slimeHeader, SLIME_HEADER)) { throw new IllegalStateException("Corrupted World!"); }

            byte version = dataStream.readByte();
            if (version > SLIME_VERSION) throw new IllegalStateException("Newer Slime Version!");

            // World version
            byte worldVersion;

            if (version >= 6) {
                worldVersion = dataStream.readByte();
            } else if (version >= 4) { // In v4 there's just a boolean indicating whether the world is pre-1.13 or post-1.13
                worldVersion = (byte) (dataStream.readBoolean() ? 0x04 : 0x01);
            } else {
                throw new UnsupportedEncodingException("SlimeVersion < 4 not supported!");
            }

            serializedDataWrapper.setSlimeHeader(slimeHeader);
            serializedDataWrapper.setSlimeVersion(version);
            serializedDataWrapper.setWorldVersion(worldVersion);

            // Chunk
            short minX = dataStream.readShort();
            short minZ = dataStream.readShort();
            int width = dataStream.readShort();
            int depth = dataStream.readShort();

            if (width <= 0 || depth <= 0) {
                throw new IllegalStateException("Corrupted World!");
            }

            serializedDataWrapper.setMinX(minX);
            serializedDataWrapper.setMinZ(minZ);
            serializedDataWrapper.setWidth(width);
            serializedDataWrapper.setDepth(depth);

            int bitmaskSize = (int) Math.ceil((width * depth) / 8.0D);
            byte[] chunkBitmask = new byte[bitmaskSize];
            dataStream.read(chunkBitmask);

            int compressedChunkDataLength = dataStream.readInt();
            int chunkDataLength = dataStream.readInt();
            byte[] compressedChunkData = new byte[compressedChunkDataLength];

            dataStream.read(compressedChunkData);

            serializedDataWrapper.setChunkBitmask(chunkBitmask);
            serializedDataWrapper.setCompressedChunkBytesLength(compressedChunkDataLength);
            serializedDataWrapper.setChunkBytesLength(chunkDataLength);
            serializedDataWrapper.setChunkBytes(compressedChunkData);

            // Tile Entities
            int compressedTileEntitiesLength = dataStream.readInt();
            int tileEntitiesLength = dataStream.readInt();
            byte[] compressedTileEntities = new byte[compressedTileEntitiesLength];

            dataStream.read(compressedTileEntities);

            serializedDataWrapper.setCompressedTileEntitiesLength(compressedTileEntitiesLength);
            serializedDataWrapper.setTileEntitiesLength(tileEntitiesLength);
            serializedDataWrapper.setTileEntities(compressedTileEntities);

            // Entities
            byte[] compressedEntities;
            boolean hasEntities;

            if (version >= 3) {
                hasEntities = dataStream.readBoolean();

                if (hasEntities) {
                    int compressedEntitiesLength = dataStream.readInt();
                    int entitiesLength = dataStream.readInt();
                    compressedEntities = new byte[compressedEntitiesLength];

                    dataStream.read(compressedEntities);

                    serializedDataWrapper.setHasEntities(true);
                    serializedDataWrapper.setCompressedEntitiesLength(compressedEntitiesLength);
                    serializedDataWrapper.setEntitiesLength(entitiesLength);
                    serializedDataWrapper.setEntities(compressedEntities);
                } else {
                    serializedDataWrapper.setHasEntities(false);
                }

            }

            // Extra NBT tag
            byte[] compressedExtraTag;

            if (version >= 2) {
                int compressedExtraTagLength = dataStream.readInt();
                int extraTagLength = dataStream.readInt();
                compressedExtraTag = new byte[compressedExtraTagLength];

                dataStream.read(compressedExtraTag);

                serializedDataWrapper.setCompressedNbtLength(compressedExtraTagLength);
                serializedDataWrapper.setNbtLength(extraTagLength);
                serializedDataWrapper.setNbtBytes(compressedExtraTag);
            }

            // World Map NBT tag
            byte[] compressedMapsTag;

            if (version >= 7) {
                int compressedMapsTagLength = dataStream.readInt();
                int mapsTagLength = dataStream.readInt();
                compressedMapsTag = new byte[compressedMapsTagLength];

                dataStream.read(compressedMapsTag);

                serializedDataWrapper.setCompressedMapsTagLength(compressedMapsTagLength);
                serializedDataWrapper.setMapsTagLength(mapsTagLength);
                serializedDataWrapper.setMapsTag(compressedMapsTag);
            }

            if (dataStream.read() != -1) {
                throw new IllegalStateException("Not at EOF!");
            }

        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serializedDataWrapper;
    }

}
