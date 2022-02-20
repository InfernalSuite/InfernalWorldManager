package com.infernalsuite.iwm.common.formats.slime;

import com.github.luben.zstd.Zstd;
import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.formats.type.SlimeFormat;
import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.common.nms.CraftInfernalWorld;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SlimeFormatImpl implements SlimeFormat {

    private static final byte[] SLIME_HEADER = new byte[]{-79,11};
    private static final byte SLIME_VERSION = 9;

    @Override
    public @NonNull SerializedDataWrapper serialize(@NonNull InfernalWorld world) {
        CraftInfernalWorld craftInfernalWorld = (CraftInfernalWorld) world;

        List<InfernalChunk> sortedChunks = craftInfernalWorld.getChunkList();

        // Ensure world properties are stored
        MutableNBTCompound extraData = craftInfernalWorld.getExtraData();
        if (extraData.containsKey("properties")) {
            extraData.remove("properties");
        }
        extraData.put("properties", craftInfernalWorld.getWorldPropertyMap().toCompound());

        SlimeSerializedDataWrapper serializedDataWrapper = new SlimeSerializedDataWrapper(getName());

        try {

            byte[] serializedFormatName = getName().getBytes(StandardCharsets.UTF_8);
            serializedDataWrapper.setFormatBytesLength(serializedFormatName.length);
            serializedDataWrapper.setFormatBytes(serializedFormatName);

            serializedDataWrapper.setSlimeHeader(SLIME_HEADER);
            serializedDataWrapper.setSlimeVersion(SLIME_VERSION);
            serializedDataWrapper.setWorldVersion(craftInfernalWorld.getVersion());

            int minX = sortedChunks.stream().mapToInt(InfernalChunk::getX).min().orElse(0);
            int minZ = sortedChunks.stream().mapToInt(InfernalChunk::getZ).min().orElse(0);
            int maxX = sortedChunks.stream().mapToInt(InfernalChunk::getX).max().orElse(0);
            int maxZ = sortedChunks.stream().mapToInt(InfernalChunk::getZ).max().orElse(0);

            serializedDataWrapper.setMinX(minX);
            serializedDataWrapper.setMinZ(minZ);

            int width = maxX - minX + 1;
            int depth = maxZ - minZ + 1;

            serializedDataWrapper.setWidth(width);
            serializedDataWrapper.setDepth(depth);

            BitSet chunkBitset = new BitSet(width * depth);
            sortedChunks.forEach(chunk -> {
                int bitsetIndex = (chunk.getZ() - minZ) * width + (chunk.getX() - minX);
                chunkBitset.set(bitsetIndex, true);
            });
            int chunkMaskSize = (int) Math.ceil((width * depth) / 8.0D);
            serializedDataWrapper.setChunkBitmask(getBitSetAsBytes(chunkBitset, chunkMaskSize));

            // Chunks
            byte[] chunkData = serializeChunks(sortedChunks, craftInfernalWorld.getVersion());
            byte[] compressedChunkData = Zstd.compress(chunkData);

            serializedDataWrapper.setCompressedChunkBytesLength(compressedChunkData.length);
            serializedDataWrapper.setChunkBytesLength(chunkData.length);
            serializedDataWrapper.setChunkBytes(compressedChunkData);

            // Tile Entities
            List<NBTCompound> tileEntitiesList = sortedChunks.stream().flatMap(chunk -> chunk.getEntities().stream()).collect(Collectors.toList());
            NBTList<NBTCompound> tileEntitiesNbtList = new NBTList<>(NBTType.TAG_Compound, tileEntitiesList);
            NBTCompound tileEntitiesCompound = new NBTCompound(Collections.singletonMap("", tileEntitiesNbtList));
            byte[] tileEntitiesData = serializeCompoundTag(tileEntitiesCompound);
            byte[] compressedTileEntitiesData = Zstd.compress(tileEntitiesData);

            serializedDataWrapper.setCompressedTileEntitiesLength(compressedTileEntitiesData.length);
            serializedDataWrapper.setTileEntitiesLength(tileEntitiesData.length);
            serializedDataWrapper.setTileEntities(compressedTileEntitiesData);

            // Entities
            List<NBTCompound> entitiesList = sortedChunks.stream().flatMap(chunk -> chunk.getEntities().stream()).collect(Collectors.toList());
            serializedDataWrapper.setHasEntities(!entitiesList.isEmpty());

            if (!entitiesList.isEmpty()) {
                NBTList<NBTCompound> entitiesNbtList = new NBTList<>(NBTType.TAG_Compound, entitiesList);
                NBTCompound entitiesCompound = new NBTCompound(Collections.singletonMap("", entitiesNbtList));
                byte[] entitiesData = serializeCompoundTag(entitiesCompound);
                byte[] compressedEntitiesData = Zstd.compress(entitiesData);

                serializedDataWrapper.setCompressedEntitiesLength(compressedEntitiesData.length);
                serializedDataWrapper.setEntitiesLength(entitiesData.length);
                serializedDataWrapper.setEntities(compressedEntitiesData);
            }

            // Extra NBT data
            byte[] extra = serializeCompoundTag(extraData.toCompound());
            byte[] compressedExtra = Zstd.compress(extra);

            serializedDataWrapper.setCompressedNbtLength(compressedExtra.length);
            serializedDataWrapper.setNbtLength(extra.length);
            serializedDataWrapper.setNbtBytes(compressedExtra);

            // World Maps
            NBTCompound mapsCompound = new NBTCompound(Collections.singletonMap("maps", new NBTList<>(NBTType.TAG_Compound, craftInfernalWorld.getWorldMaps())));
            byte[] mapsData = serializeCompoundTag(mapsCompound);
            byte[] compressedMapsData = Zstd.compress(mapsData);

            serializedDataWrapper.setCompressedMapsTagLength(compressedMapsData.length);
            serializedDataWrapper.setMapsTagLength(mapsData.length);
            serializedDataWrapper.setMapsTag(compressedMapsData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return serializedDataWrapper;
    }

    @Override
    public @NonNull CraftInfernalWorld deserialize(@NonNull SerializedDataWrapper serializedWorld) {
        return null;
    }

    private static byte[] getBitSetAsBytes(BitSet set, int fixedSize) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream outStream = new DataOutputStream(byteArrayOutputStream);

        try {
            byte[] arr = set.toByteArray();
            outStream.write(arr);

            int chunkMaskPadding = fixedSize - arr.length;
            for (int i = 0; i < chunkMaskPadding; i++) outStream.write(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] serializeChunks(List<InfernalChunk> chunks, byte worldVersion) throws IOException {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream(8192);
        DataOutputStream outStream = new DataOutputStream(outByteStream);

        for (InfernalChunk chunk : chunks) {

            // Height Maps
            byte[] heightMaps = serializeCompoundTag(chunk.getHeightMaps());
            outStream.writeInt(heightMaps.length);
            outStream.write(heightMaps);

            // Biomes (pre 1.18)
            if (worldVersion < 0x08) {
                int[] biomes = chunk.getBiomes();
                outStream.writeInt(biomes.length);
                for (int biome : biomes) outStream.writeInt(biome);
            }

            // Chunk Sections
            InfernalChunkSection[] sections = chunk.getSections();
            if (worldVersion < 0x07) { // Pre 1.17
                BitSet sectionBitMask = new BitSet(16);

                for (int i = 0; i < sections.length; i++) {
                    sectionBitMask.set(i, sections[i] != null);
                }

                outStream.write(getBitSetAsBytes(sectionBitMask, 2));

                for (InfernalChunkSection section : sections) {
                    if (section == null) continue;

                    // === Block Light ===
                    boolean hasBlockLight = section.getBlockLight() != null;
                    outStream.writeBoolean(hasBlockLight);
                    if (hasBlockLight) outStream.write(section.getBlockLight().getBackingArray());

                    // === Block Data ===

                    // Palette
                    List<NBTCompound> palette = section.getPalette().asListView(); // Ignore NPE warning - never null on worldVersion < 0x07
                    outStream.writeInt(palette.size());
                    for (NBTCompound value : palette) {
                        byte[] serializedValue = serializeCompoundTag(value);
                        outStream.writeInt(serializedValue.length);
                        outStream.write(serializedValue);
                    }

                    // Block States
                    long[] blockStates = section.getBlockStates();
                    outStream.writeInt(blockStates.length);
                    for (long value : blockStates) outStream.writeLong(value);

                    // === Sky Light ===
                    boolean hasSkyLight = section.getSkyLight() != null;
                    outStream.writeBoolean(hasSkyLight);
                    if (hasSkyLight) outStream.write(section.getSkyLight().getBackingArray());
                }

            } else { // 1.17+
                outStream.writeInt(chunk.getMinSection());
                outStream.writeInt(chunk.getMaxSection());
                outStream.writeInt(Math.toIntExact(Arrays.stream(sections).filter(Objects::nonNull).count()));

                for (int i = 0; i < sections.length; i++) {
                    InfernalChunkSection section = sections[i];
                    if (section == null) continue;

                    outStream.writeInt(i);

                    // Block Light
                    boolean hasBlockLight = section.getBlockLight() != null;
                    outStream.writeBoolean(hasBlockLight);
                    if (hasBlockLight) outStream.write(section.getBlockLight().getBackingArray());

                    // Block Data
                    byte[] serializedBlockStates = serializeCompoundTag(section.getBlockStatesTag());
                    outStream.writeInt(serializedBlockStates.length);
                    outStream.write(serializedBlockStates);

                    // Biome Data
                    byte[] serializedBiomes = serializeCompoundTag(section.getBiomeTag());
                    outStream.writeInt(serializedBiomes.length);
                    outStream.write(serializedBiomes);

                    // Sky Light
                    boolean hasSkyLight = section.getSkyLight() != null;
                    outStream.writeBoolean(hasSkyLight);
                    if (hasSkyLight) outStream.write(section.getSkyLight().getBackingArray());
                }
            }
        }

        return outByteStream.toByteArray();
    }

    private static byte[] serializeCompoundTag(NBTCompound tag) throws IOException {
        if (tag == null || tag.isEmpty()) return new byte[0];
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        NBTWriter outStream = new NBTWriter(outByteStream, CompressedProcesser.NONE);
        outStream.writeRaw(tag);
        return outByteStream.toByteArray();
    }

    private static NBTCompound readCompoundTag(byte[] serializedCompound) throws IOException, NBTException {
        if (serializedCompound.length == 0) return null;

        NBTReader reader = new NBTReader(new ByteArrayInputStream(serializedCompound), CompressedProcesser.NONE);
        return (NBTCompound) reader.read();
    }
}
