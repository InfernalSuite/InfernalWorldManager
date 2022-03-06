package com.infernalsuite.iwm.common.formats.slime;

import com.github.luben.zstd.Zstd;
import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.formats.type.SlimeFormat;
import com.infernalsuite.iwm.api.utils.NBTUtil;
import com.infernalsuite.iwm.api.utils.NibbleArray;
import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import com.infernalsuite.iwm.common.nms.CraftInfernalChunk;
import com.infernalsuite.iwm.common.nms.CraftInfernalChunkSection;
import com.infernalsuite.iwm.common.nms.CraftInfernalWorld;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.*;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SlimeFormatImpl implements SlimeFormat {

    public static final byte[] SLIME_HEADER = new byte[]{-79,11};
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

        SlimeSerializedDataWrapper serializedDataWrapper = new SlimeSerializedDataWrapper(
                getName(),
                world.getName(),
                world.getLoader(),
                world.getWorldPropertyMap(),
                world.isReadOnly()
        );

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
    public @NonNull CraftInfernalWorld deserialize(@NonNull SerializedDataWrapper serializedWorld) throws IOException {
        if (serializedWorld instanceof SlimeSerializedDataWrapper serializedDataWrapper
                && StandardCharsets.UTF_8.decode(ByteBuffer.wrap(serializedDataWrapper.getFormatBytes())).toString().equals(getName())) {

            if (serializedDataWrapper.getWidth() <= 0 || serializedDataWrapper.getDepth() <= 0) {
                // TODO: CorruptedWorld
            }

            // Chunk decompression & deserialization
            Map<Long, InfernalChunk> chunks = readChunks(serializedDataWrapper.getWorldName(), serializedDataWrapper);

            // Other Data Decompression
            byte[] tileEntitiesData = new byte[serializedDataWrapper.getTileEntitiesLength()];
            byte[] entitiesData = new byte[serializedDataWrapper.getEntitiesLength()];
            byte[] extraNbtData = new byte[serializedDataWrapper.getNbtLength()];
            byte[] mapsData = new byte[serializedDataWrapper.getMapsTagLength()];

            Zstd.decompress(tileEntitiesData, serializedDataWrapper.getTileEntities());
            Zstd.decompress(entitiesData, serializedDataWrapper.getEntities());
            Zstd.decompress(extraNbtData, serializedDataWrapper.getNbtBytes());
            Zstd.decompress(mapsData, serializedDataWrapper.getMapsTag());

            // Entities Deserialization
            NBTCompound entitiesCompound = readCompoundTag(entitiesData);
            if (entitiesCompound != null) {
                NBTList<NBTCompound> entitiesList = entitiesCompound.getList("");
                for (NBTCompound entityTag : entitiesList.asListView()) {
                    NBTList<NBTDouble> listTag = entityTag.getList("Pos");
                    int cx = floor(listTag.get(0).getValue()) >> 4;
                    int cz = floor(listTag.get(2).getValue()) >> 4;
                    long chunkKey = CraftInfernalWorld.calcIndex(cx, cz);
                    InfernalChunk chunk = chunks.get(chunkKey);
                    if (chunk == null) {
                        // TODO: Corrupted World Exception
                    }
                    chunk.getEntities().add(entityTag);
                }
            }

            // Tile Entities Deserialization
            NBTCompound tileEntitiesCompound = readCompoundTag(tileEntitiesData);
            if (tileEntitiesCompound != null) {
                NBTList<NBTCompound> tileEntitiesList = entitiesCompound.getList("");
                for (NBTCompound tileEntityTag : tileEntitiesList.asListView()) {
                    int cx = floor(tileEntityTag.getAsInt("x") >> 4);
                    int cz = floor(tileEntityTag.getAsInt("z") >> 4);
                    long chunkKey = CraftInfernalWorld.calcIndex(cx, cz);
                    InfernalChunk chunk = chunks.get(chunkKey);
                    if (chunk == null) {
                        // TODO: Corrupted World Exception
                    }
                    chunk.getTileEntities().add(tileEntityTag);
                }
            }

            // Extra Data
            NBTCompound extraNbtTag = readCompoundTag(extraNbtData);
            MutableNBTCompound extraTag = extraNbtTag == null ? new MutableNBTCompound() : extraNbtTag.toMutableCompound();

            // World Maps
            NBTCompound mapsCompound = readCompoundTag(mapsData);
            List<NBTCompound> mapsList = mapsCompound == null
                    ? new ArrayList<>()
                    : (List<NBTCompound>) NBTUtil.getAsListTag(mapsCompound.getList("maps")).map(NBTList::asListView).orElseGet(ArrayList::new);

            // Extract World Properties
            WorldPropertyMap worldPropertyMap;
            Optional<NBTCompound> propertiesMap = Optional.ofNullable(extraTag.getCompound("properties"));
            worldPropertyMap = propertiesMap.map(entries -> new WorldPropertyMap(entries.asMapView())).orElseGet(WorldPropertyMap::new);

            return new CraftInfernalWorld(
                    serializedDataWrapper.getLoader(),
                    serializedDataWrapper.getWorldName(),
                    chunks,
                    extraTag,
                    mapsList,
                    serializedDataWrapper.getWorldVersion(),
                    worldPropertyMap,
                    serializedDataWrapper.isReadOnly(),
                    !serializedDataWrapper.isReadOnly(),
                    serializedDataWrapper.getFormatName()
            );

        } else {
            throw new IllegalStateException("Slime Format Implementation cannot deserialize non-slime format data!");
        }
    }

    //region Serialization Helpers
    //=========================================================================

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

    //=========================================================================
    //endregion

    //region Deserialization Helpers
    //=========================================================================

    private static NBTCompound readCompoundTag(byte[] serializedCompound) throws IOException {
        if (serializedCompound.length == 0) return null;

        NBTReader reader = new NBTReader(new ByteArrayInputStream(serializedCompound), CompressedProcesser.NONE);
        try {
            return (NBTCompound) reader.read();
        } catch (NBTException e) {
            throw new IOException(e);
        }
    }

    private static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    private static Map<Long, InfernalChunk> readChunks(String worldName, SlimeSerializedDataWrapper serializedDataWrapper) throws IOException {
        byte[] chunkData = new byte[serializedDataWrapper.getChunkBytesLength()];
        Zstd.decompress(chunkData, serializedDataWrapper.getChunkBytes());
        DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(chunkData));
        Map<Long, InfernalChunk> chunkMap = new HashMap<>();

        BitSet chunkBitset = BitSet.valueOf(serializedDataWrapper.getChunkBitmask());
        int depth = serializedDataWrapper.getDepth();
        int width = serializedDataWrapper.getWidth();

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                int bitsetIndex = z * width + x;

                if (chunkBitset.get(bitsetIndex)) {

                    // Height Maps
                    NBTCompound heightMaps;
                    int heightMapsLength = dataStream.readInt();
                    byte[] heightMapsArray = new byte[heightMapsLength];
                    dataStream.read(heightMapsArray);
                    heightMaps = readCompoundTag(heightMapsArray);
                    if (heightMaps == null) heightMaps = new NBTCompound(Collections.singletonMap("", new MutableNBTCompound(new HashMap<>()).toCompound()));

                    // Biome Data (Pre 1.18)
                    int[] biomes = null;
                    if (serializedDataWrapper.getWorldVersion() < 0x08) {
                        int biomesArrayLength = serializedDataWrapper.getSlimeVersion() >= 8 ? dataStream.readInt() : 256;
                        biomes = new int[biomesArrayLength];
                        for (int i = 0; i < biomes.length; i++) biomes[i] = dataStream.readInt();
                    }

                    ChunkSectionData data = serializedDataWrapper.getWorldVersion() < 0x08
                            ? readChunkSections(dataStream, serializedDataWrapper.getWorldVersion(), serializedDataWrapper.getSlimeVersion())
                            : readChunkSectionsNew(dataStream, serializedDataWrapper.getWorldVersion(), serializedDataWrapper.getSlimeVersion());

                    chunkMap.put(((long) serializedDataWrapper.getMinZ() + z) * Integer.MAX_VALUE + ((long) serializedDataWrapper.getMinX() + x),
                            new CraftInfernalChunk(worldName, serializedDataWrapper.getMinX() + x, serializedDataWrapper.getMinZ() + z, data.sections,
                                    heightMaps, biomes, new ArrayList<>(), new ArrayList<>(), data.minSectionY, data.maxSectionY));
                }
            }
        }

        return chunkMap;
    }

    private static int[] toIntArray(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN);
        int[] ret = new int[buf.length / 4];
        buffer.asIntBuffer().get(ret);
        return ret;
    }

    @Data
    private static class ChunkSectionData {
        private final InfernalChunkSection[] sections;
        private final int minSectionY;
        private final int maxSectionY;
    }

    private static ChunkSectionData readChunkSectionsNew(DataInputStream dataStream, int worldVersion, int version) throws IOException {
        int minSectionY = dataStream.readInt();
        int maxSectionY = dataStream.readInt();
        int sectionCount = dataStream.readInt();
        InfernalChunkSection[] chunkSectionArray = new InfernalChunkSection[maxSectionY - minSectionY];

        for (int i = 0; i < sectionCount; i++) {
            int y = dataStream.readInt();

            // Block Light
            NibbleArray blockLightArray;
            if (dataStream.readBoolean()) {
                byte[] blockLightByteArray = new byte[1024];
                dataStream.read(blockLightByteArray);
                blockLightArray = new NibbleArray(blockLightByteArray);
            } else blockLightArray = null;

            // Block Data
            byte[] blockStateData = new byte[dataStream.readInt()];
            dataStream.read(blockStateData);
            NBTCompound blockStateTag = readCompoundTag(blockStateData);

            // Biome Data
            byte[] biomeData = new byte[dataStream.readInt()];
            dataStream.read(biomeData);
            NBTCompound biomeTag = readCompoundTag(biomeData);

            // Sky Light Data
            NibbleArray skyLightArray;
            if (dataStream.readBoolean()) {
                byte[] skyLightByteArray = new byte[2048];
                dataStream.read(skyLightByteArray);
                skyLightArray = new NibbleArray(skyLightByteArray);
            } else skyLightArray = null;

            chunkSectionArray[y] = new CraftInfernalChunkSection(null, null, blockStateTag, biomeTag, blockLightArray, skyLightArray);
        }

        return new ChunkSectionData(chunkSectionArray, minSectionY, maxSectionY);
    }

    private static ChunkSectionData readChunkSections(DataInputStream dataStream, byte worldVersion, int version) throws IOException {
        InfernalChunkSection[] chunkSectionArray = new InfernalChunkSection[16];
        byte[] sectionBitmask = new byte[2];
        dataStream.read(sectionBitmask);
        BitSet sectionBitset = BitSet.valueOf(sectionBitmask);

        for (int i = 0; i < 16; i++) {
            if (sectionBitset.get(i)) {
                // Block Light
                NibbleArray blockLightArray;
                if (dataStream.readBoolean()) {
                    byte[] blockLightByteArray = new byte[2048];
                    dataStream.read(blockLightByteArray);
                    blockLightArray = new NibbleArray(blockLightByteArray);
                } else blockLightArray = null;

                // Block Data
                byte[] blockArray = null;
                NibbleArray dataArray = null;

                NBTList<NBTCompound> paletteTag = null;
                long[] blockStatesArray = null;

                byte[] rawBlockStates = null;
                byte[] rawBiomes = null;

                int palettelength = dataStream.readInt();
                List<NBTCompound> paletteList = new ArrayList<>(palettelength);
                for (int index = 0; index < palettelength; index++) {
                    int tagLength = dataStream.readInt();
                    byte[] serializedTag = new byte[tagLength];
                    dataStream.read(serializedTag);

                    NBTCompound tag = readCompoundTag(serializedTag);
                    paletteList.add(tag);
                }
                paletteTag = new NBTList<>(NBTType.TAG_Compound, paletteList);

                // Block States
                int blockStatesArrayLength = dataStream.readInt();
                blockStatesArray = new long[blockStatesArrayLength];

                for (int index = 0; index < blockStatesArrayLength; index++) blockStatesArray[index] = dataStream.readLong();

                // Sky Light
                NibbleArray skyLightArray;

                if (dataStream.readBoolean()) {
                    byte[] skyLightByteArray = new byte[2048];
                    dataStream.read(skyLightByteArray);
                    skyLightArray = new NibbleArray(skyLightByteArray);
                } else skyLightArray = null;

                chunkSectionArray[i] = new CraftInfernalChunkSection(paletteTag, blockStatesArray, null, null, blockLightArray, skyLightArray);
            }
        }

        return new ChunkSectionData(chunkSectionArray, 0, 16);
    }

    //=========================================================================
    //endregion

}
