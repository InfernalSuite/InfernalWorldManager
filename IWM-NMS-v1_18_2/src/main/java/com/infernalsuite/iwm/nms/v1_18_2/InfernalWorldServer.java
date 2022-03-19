package com.infernalsuite.iwm.nms.v1_18_2;

import ca.spottedleaf.starlight.common.light.SWMRNibbleArray;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalChunkSection;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import com.infernalsuite.iwm.common.nms.CraftInfernalWorld;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTLongArray;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class InfernalWorldServer extends ServerLevel {

    private static final ExecutorService WORLD_SAVER_SERVICE = Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("IWM Pool Thread #%1$d").build());

    private final Object saveLock = new Object();

    @Getter
    private final CraftInfernalWorld infernalWorld;
    private final BiomeSource defaultBiomeSource;
    private final FormatRegistry formatRegistry;

    @Getter @Setter
    private boolean ready = false;

    public InfernalWorldServer(CraftInfernalWorld world, FormatRegistry formatRegistry, ServerLevelData worldData,
                               ResourceKey<Level> worldKey, ResourceKey<LevelStem> dimensionKey,
                               Holder<DimensionType> dimensionManager, ChunkGenerator chunkGenerator,
                               World.Environment environment) throws IOException {
        super(MinecraftServer.getServer(), MinecraftServer.getServer().executor,
                v1182InfernalNMS.CUSTOM_LEVEL_STORAGE.createAccess(world.getName(), dimensionKey),
                worldData, worldKey, dimensionManager, MinecraftServer.getServer().progressListenerFactory.create(11),
                chunkGenerator, false, 0, new ArrayList<>(), true, environment, null, null);

        this.infernalWorld = world;
        this.formatRegistry = formatRegistry;

        WorldPropertyMap propertyMap = world.getWorldPropertyMap();

        this.serverLevelData.setDifficulty(Difficulty.valueOf(propertyMap.getValue(WorldProperties.DIFFICULTY).toUpperCase()));
        this.serverLevelData.setSpawn(new BlockPos(
                propertyMap.getValue(WorldProperties.SPAWN_X),
                propertyMap.getValue(WorldProperties.SPAWN_Y),
                propertyMap.getValue(WorldProperties.SPAWN_Z)
                ), 0);
        super.setSpawnSettings(propertyMap.getValue(WorldProperties.ALLOW_MONSTERS), propertyMap.getValue(WorldProperties.ALLOW_ANIMALS));

        this.pvpMode = propertyMap.getValue(WorldProperties.PVP);

        String biomeStr = infernalWorld.getWorldPropertyMap().getValue(WorldProperties.DEFAULT_BIOME);
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biomeStr));
        Holder<Biome> defaultBiome = MinecraftServer.getServer().registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).getHolder(biomeKey).orElseThrow();

        this.defaultBiomeSource = new FixedBiomeSource(defaultBiome);

        this.keepSpawnInMemory = false;
    }

    @Override
    public void save(@Nullable ProgressListener progressListener, boolean flush, boolean savingDisabled) {
        if (!infernalWorld.isReadOnly() && !savingDisabled) {
            Bukkit.getPluginManager().callEvent(new WorldSaveEvent(getWorld()));

            this.getChunkSource().save(flush);
            this.serverLevelData.setWorldBorder(this.getWorldBorder().createSettings());
            this.serverLevelData.setCustomBossEvents(MinecraftServer.getServer().getCustomBossEvents().save());

            CompoundTag compoundTag = new CompoundTag();
            CompoundTag nbtTagCompound = this.serverLevelData.createTag(MinecraftServer.getServer().registryAccess(), compoundTag);
            infernalWorld.getExtraData().put("LevelData", Converter.convertToNBT(nbtTagCompound));

            if (MinecraftServer.getServer().isStopped()) {
                save();

                try {
                    infernalWorld.getLoader().unlockWorld(infernalWorld.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                WORLD_SAVER_SERVICE.execute(this::save);
            }
        }
    }

    private void save() {
        synchronized (saveLock) {
            formatRegistry.getFormat(infernalWorld.getFormatName()).thenAccept(optFormat -> {
                if (optFormat.isPresent()) {
                    Format format = optFormat.get();
                    Bukkit.getLogger().log(java.util.logging.Level.INFO, "Saving world " + infernalWorld.getName() + "...");
                    long serializationStart = System.currentTimeMillis();
                    SerializedDataWrapper serializedDataWrapper = format.serialize(infernalWorld);
                    long saveStart = System.currentTimeMillis();
                    infernalWorld.getLoader().saveWorld(infernalWorld.getName(), serializedDataWrapper, false);
                    Bukkit.getLogger().log(java.util.logging.Level.INFO, "World " + infernalWorld.getName() + " serialized in " +
                            (saveStart - serializationStart) + "ms, and save in " + (System.currentTimeMillis() - saveStart) + "ms");
                } else {
                    throw new IllegalStateException("Failed to get format - Cannot save world: " + infernalWorld.getName());
                }
            });
        }
    }

    ImposterProtoChunk getImposterChunk(int cx, int cz) {
        InfernalChunk infernalChunk = infernalWorld.getChunk(cx, cz);
        LevelChunk chunk;

        if (infernalChunk instanceof NMSInfernalChunk) {
            chunk = ((NMSInfernalChunk) infernalChunk).getChunk();
        } else {
            if (infernalChunk == null) {
                ChunkPos pos = new ChunkPos(cx, cz);

                LevelChunkTicks<Block> blockLevelChunkTicks = new LevelChunkTicks<>();
                LevelChunkTicks<Fluid> fluidLevelChunkTicks = new LevelChunkTicks<>();

                chunk = new LevelChunk(this, pos, UpgradeData.EMPTY, blockLevelChunkTicks, fluidLevelChunkTicks,
                        0L, null, null, null);
            } else {
                chunk = createChunk(infernalChunk);
            }

            infernalWorld.updateChunk(new NMSInfernalChunk(chunk));
        }

        return new ImposterProtoChunk(chunk, false);
    }

    private LevelChunk createChunk(InfernalChunk chunk) {
        int cx = chunk.getX();
        int cz = chunk.getZ();
        ChunkPos pos = new ChunkPos(cx, cz);

        LevelChunkSection[] sections = new LevelChunkSection[this.getSectionsCount()];

        Object[] blockNibbles = null;
        Object[] skyNibbles = null;
        if (v1182InfernalNMS.isPaperMC) {
            blockNibbles = ca.spottedleaf.starlight.common.light.StarLightEngine.getFilledEmptyLight(this);
            skyNibbles = ca.spottedleaf.starlight.common.light.StarLightEngine.getFilledEmptyLight(this);
            getServer().scheduleOnMain(() -> getLightEngine().retainData(pos, true));
        }

        Registry<Biome> biomeRegistry = this.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
        Codec<PalettedContainer<Holder<Biome>>> codec = PalettedContainer.codec(biomeRegistry.asHolderIdMap(), biomeRegistry.holderByNameCodec(),
                PalettedContainer.Strategy.SECTION_BIOMES, biomeRegistry.getHolderOrThrow(Biomes.PLAINS));

        for (int sectionId = 0; sectionId < chunk.getSections().length; sectionId++) {
            InfernalChunkSection infernalChunkSection = chunk.getSections()[sectionId];

            if (infernalChunkSection != null) {
                BlockState[] presetBlockStates = null;
                if (v1182InfernalNMS.isPaperMC) {
                    blockNibbles[sectionId] = new ca.spottedleaf.starlight.common.light.SWMRNibbleArray(infernalChunkSection.getBlockLight().getBackingArray());
                    skyNibbles[sectionId] = new ca.spottedleaf.starlight.common.light.SWMRNibbleArray(infernalChunkSection.getSkyLight().getBackingArray());
                    presetBlockStates = this.chunkPacketBlockController.getPresetBlockStates(this, pos, sectionId << 4);
                }

                PalettedContainer<BlockState> blockPalette;
                if (infernalChunkSection.getBlockStatesTag() != null) {
                    Codec<PalettedContainer<BlockState>> blockStateCodec = presetBlockStates == null
                            ? ChunkSerializer.BLOCK_STATE_CODEC
                            : PalettedContainer.codec(
                              Block.BLOCK_STATE_REGISTRY,
                              BlockState.CODEC,
                              PalettedContainer.Strategy.SECTION_STATES,
                              Blocks.AIR.defaultBlockState(),
                              presetBlockStates
                            );
                    DataResult<PalettedContainer<BlockState>> dataResult = blockStateCodec.parse(
                            NbtOps.INSTANCE,
                            Converter.convertToTag(infernalChunkSection.getBlockStatesTag()))
                                .promotePartial(s -> System.out.println("Recoverable error when parsing section " + cx + "," + cz + ": " + s));
                    blockPalette = dataResult.getOrThrow(false, System.err::println);
                } else {
                    blockPalette = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES, presetBlockStates);
                }

                PalettedContainer<Holder<Biome>> biomePalette;

                if (infernalChunkSection.getBiomeTag() != null) {
                    DataResult<PalettedContainer<Holder<Biome>>> dataResult = codec.parse(NbtOps.INSTANCE, Converter.convertToTag(infernalChunkSection.getBiomeTag()))
                            .promotePartial(s -> System.out.println("Recoverable error when parsing section " + cx + "," + cz + ": " + s));
                    biomePalette = dataResult.getOrThrow(false, System.err::println);
                } else {
                    biomePalette = new PalettedContainer<>(biomeRegistry.asHolderIdMap(),
                            biomeRegistry.getHolderOrThrow(Biomes.PLAINS),
                            PalettedContainer.Strategy.SECTION_BIOMES);
                }

                LevelChunkSection section = new LevelChunkSection(sectionId << 4, blockPalette, biomePalette);
                sections[sectionId] = section;
            }
        }

        LevelChunk.PostLoadProcessor loadEntities = (nmsChunk) -> {

            List<NBTCompound> tileEntities = chunk.getTileEntities();

            if (tileEntities != null) {
                for (NBTCompound nbt : tileEntities) {
                    Optional<String> type = Optional.ofNullable(nbt.getString("id"));

                    if (type.isPresent()) {
                        BlockPos blockPosition = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
                        BlockState blockData = nmsChunk.getBlockState(blockPosition);
                        BlockEntity entity = BlockEntity.loadStatic(blockPosition, blockData, (CompoundTag) Converter.convertToTag(nbt));
                        if (entity != null) nmsChunk.setBlockEntity(entity);
                    }
                }
            }

            List<NBTCompound> entities = chunk.getEntities();
            if (entities != null) {
                this.entityManager.addLegacyChunkEntities(EntityType.loadEntitiesRecursive(entities.stream()
                        .map(nbt -> (CompoundTag) Converter.convertToTag(nbt))
                        .collect(Collectors.toList()), this));
            }
        };

        LevelChunkTicks<Block> blockLevelChunkTicks = new LevelChunkTicks<>();
        LevelChunkTicks<Fluid> fluidLevelChunkTicks = new LevelChunkTicks<>();
        LevelChunk nmsChunk = new LevelChunk(this, pos,
                UpgradeData.EMPTY,
                blockLevelChunkTicks, fluidLevelChunkTicks, 0L, sections, loadEntities, null);

        EnumSet<Heightmap.Types> heightMapTypes = nmsChunk.getStatus().heightmapsAfter();
        Map<String, NBT> heightMaps = chunk.getHeightMaps().asMapView();
        EnumSet<Heightmap.Types> unsetHeightMaps = EnumSet.noneOf(Heightmap.Types.class);

        if (v1182InfernalNMS.isPaperMC) {
            nmsChunk.setBlockNibbles((SWMRNibbleArray[]) blockNibbles);
            nmsChunk.setSkyNibbles((SWMRNibbleArray[]) skyNibbles);
        }

        for (Heightmap.Types type : heightMapTypes) {
            String name = type.getSerializedName();

            if (heightMaps.containsKey(name)) {
                NBTLongArray heightMap = (NBTLongArray) heightMaps.get(name);
                nmsChunk.setHeightmap(type, heightMap.getValue().copyArray());
            } else {
                unsetHeightMaps.add(type);
            }
        }

        if (!unsetHeightMaps.isEmpty()) Heightmap.primeHeightmaps(nmsChunk, unsetHeightMaps);

        return nmsChunk;
    }

    void saveChunk(LevelChunk chunk) {
        InfernalChunk infernalChunk = infernalWorld.getChunk(chunk.getPos().x, chunk.getPos().z);

        if (infernalChunk instanceof NMSInfernalChunk) {
            ((NMSInfernalChunk) infernalChunk).setChunk(chunk);
        } else {
            infernalWorld.updateChunk(new NMSInfernalChunk(chunk));
        }
    }
}
