package com.infernalsuite.iwm.nms.v1_18_2;

import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import com.infernalsuite.iwm.common.nms.CraftInfernalWorld;
import com.infernalsuite.iwm.common.nms.InfernalNMS;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import lombok.Getter;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelVersion;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.scoreboard.CraftScoreboardManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;

@Getter
public class v1182InfernalNMS implements InfernalNMS<World> {

    private static final Logger LOGGER = LogManager.getLogger("IWM");
    public static boolean isPaperMC;

    public static LevelStorageSource CUSTOM_LEVEL_STORAGE;

    static {
        try {
            Path path = Files.createTempDirectory("iwm-" + UUID.randomUUID().toString().substring(0, 5)).toAbsolutePath();
            CUSTOM_LEVEL_STORAGE = new LevelStorageSource(path, path, DataFixers.getDataFixer());

            FileUtils.forceDeleteOnExit(path.toFile());
        } catch (IOException ex) {
            throw new IllegalStateException("Couldn't create dummy file directory", ex);
        }
    }

    private final byte worldVersion = 0x08;

    private boolean injectFakeDimensions = false;

    private final FormatRegistry formatRegistry;

    private InfernalWorldServer defaultWorld;
    private InfernalWorldServer defaultNetherWorld;
    private InfernalWorldServer defaultEndWorld;

    public v1182InfernalNMS(FormatRegistry formatRegistry, boolean isPaper) {
        this.formatRegistry = formatRegistry;
        try {
            isPaperMC = isPaper;
            CraftCLSMBridge.initialize(this);
        } catch (NoClassDefFoundError ex) {
            LOGGER.error("Failed to find ClassModifier classes. Are you sure you installed it correctly?", ex);
            Bukkit.getServer().shutdown();
        }
    }

    @Override
    public Object injectDefaultWorlds() {
        if (!injectFakeDimensions) return null;

        System.out.println("Injecting: " + defaultEndWorld + " " + defaultNetherWorld + " " + defaultEndWorld);

        MinecraftServer server = MinecraftServer.getServer();
        server.server.scoreboardManager = new CraftScoreboardManager(server, server.getScoreboard());

        if (defaultWorld != null) registerWorld(defaultWorld);
        if (defaultNetherWorld != null) registerWorld(defaultNetherWorld);
        if (defaultEndWorld != null) registerWorld(defaultEndWorld);

        injectFakeDimensions = false;
        return new MappedRegistry<>(Registry.ACTIVITY_REGISTRY, Lifecycle.stable(), null);
    }

    @Override
    public void setDefaultWorlds(InfernalWorld normalWorld, InfernalWorld netherWorld, InfernalWorld endWorld) throws IOException {
        try {
            MinecraftServer server = MinecraftServer.getServer();

            LevelSettings worldSettings;
            WorldGenSettings generatorSettings;

            DedicatedServerProperties dedicatedServerProperties = ((DedicatedServer) server).getProperties();

            worldSettings = new LevelSettings(dedicatedServerProperties.levelName, dedicatedServerProperties.gamemode,
                    dedicatedServerProperties.hardcore, dedicatedServerProperties.difficulty, false,
                    new GameRules(), server.datapackconfiguration);
            generatorSettings = dedicatedServerProperties.getWorldGenSettings(server.registryAccess());

            WorldData data = new PrimaryLevelData(worldSettings, generatorSettings, Lifecycle.stable());

            var field = MinecraftServer.class.getDeclaredField("p");
            field.setAccessible(true);
            field.set(server, data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (normalWorld != null) {
            normalWorld.getWorldPropertyMap().setValue(WorldProperties.ENVIRONMENT, World.Environment.NORMAL.toString().toLowerCase());
            defaultWorld = createCustomWorld(normalWorld, Level.OVERWORLD);
            injectFakeDimensions = true;
        }

        if (netherWorld != null) {
            netherWorld.getWorldPropertyMap().setValue(WorldProperties.ENVIRONMENT, World.Environment.NETHER.toString().toLowerCase());
            defaultNetherWorld = createCustomWorld(netherWorld, Level.NETHER);
            injectFakeDimensions = true;
        }

        if (endWorld != null) {
            endWorld.getWorldPropertyMap().setValue(WorldProperties.ENVIRONMENT, World.Environment.THE_END.toString().toLowerCase());
            defaultEndWorld = createCustomWorld(endWorld, Level.END);
            injectFakeDimensions = true;
        }
    }

    private InfernalWorldServer createCustomWorld(InfernalWorld world, @Nullable ResourceKey<Level> dimensionOverride) {
        String worldName = world.getName();

        PrimaryLevelData worldDataServer = createWorldData(world);
        World.Environment environment = getEnvironment(world);
        ResourceKey<LevelStem> dimension = switch (environment) {
            case NORMAL -> LevelStem.OVERWORLD;
            case NETHER -> LevelStem.NETHER;
            case THE_END -> LevelStem.END;
            default -> throw new IllegalArgumentException("Unknown dimension supplied!");
        };

        Registry<LevelStem> registryMaterials = worldDataServer.worldGenSettings().dimensions();
        LevelStem worldDimension = registryMaterials.get(dimension);

        DimensionType predefinedType = worldDimension.typeHolder().value();

        OptionalLong fixedTime = switch (environment) {
            case NORMAL -> OptionalLong.empty();
            case NETHER -> OptionalLong.of(18000L);
            case THE_END -> OptionalLong.of(6000L);
            case CUSTOM -> throw new UnsupportedOperationException();
        };

        double light = switch (environment) {
            case NORMAL, THE_END -> 0;
            case NETHER -> 0.1;
            case CUSTOM -> throw new UnsupportedOperationException();
        };

        TagKey<Block> infiniburn = switch (environment) {
            case NORMAL -> BlockTags.INFINIBURN_OVERWORLD;
            case NETHER -> BlockTags.INFINIBURN_NETHER;
            case THE_END -> BlockTags.INFINIBURN_END;
            case CUSTOM -> throw new UnsupportedOperationException();
        };

        Holder<DimensionType> type = Holder.direct(DimensionType.create(fixedTime, predefinedType.hasSkyLight(), predefinedType.hasCeiling(),
                predefinedType.ultraWarm(), predefinedType.natural(), predefinedType.coordinateScale(),
                world.getWorldPropertyMap().getValue(WorldProperties.DRAGON_BATTLE), predefinedType.piglinSafe(), predefinedType.bedWorks(),
                predefinedType.respawnAnchorWorks(), predefinedType.hasRaids(), predefinedType.minY(), predefinedType.height(), predefinedType.logicalHeight(),
                infiniburn, predefinedType.effectsLocation(), (float) light));

        ChunkGenerator chunkGenerator = worldDimension.generator();

        ResourceKey<Level> worldKey = dimensionOverride == null ? ResourceKey.create(Registry.DIMENSION_REGISTRY,
                new ResourceLocation(worldName.toLowerCase(java.util.Locale.ENGLISH))) : dimensionOverride;

        InfernalWorldServer level;

        try {
            level = new InfernalWorldServer((CraftInfernalWorld) world, formatRegistry, worldDataServer, worldKey, dimension,
                    type, chunkGenerator, environment);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // TODO: Something better here
        }

        level.setReady(true);
        level.setSpawnSettings(world.getWorldPropertyMap().getValue(WorldProperties.ALLOW_MONSTERS), world.getWorldPropertyMap().getValue(WorldProperties.ALLOW_ANIMALS));

        return level;
    }

    private World.Environment getEnvironment(InfernalWorld world) {
        return World.Environment.valueOf(world.getWorldPropertyMap().getValue(WorldProperties.ENVIRONMENT).toUpperCase());
    }

    private PrimaryLevelData createWorldData(InfernalWorld world) {
        String worldName = world.getName();
        NBTCompound extraData = world.getExtraData().toCompound();
        PrimaryLevelData worldDataServer;
        CompoundTag extraTag = (CompoundTag) Converter.convertToTag(extraData);
        MinecraftServer mcServer = MinecraftServer.getServer();
        DedicatedServerProperties serverProperties = ((DedicatedServer) mcServer).getProperties();

        if (extraTag.getTagType("LevelData") == Tag.TAG_COMPOUND) {
            CompoundTag levelData = extraTag.getCompound("LevelData");
            int dataVersion = levelData.getTagType("DataVersion") == Tag.TAG_INT ? levelData.getInt("DataVersion") : -1;
            Dynamic<Tag> dynamic = mcServer.getFixerUpper().update(DataFixTypes.LEVEL.getType(),
                    new Dynamic<>(NbtOps.INSTANCE, levelData), dataVersion, SharedConstants.getCurrentVersion().getWorldVersion());

            LevelVersion levelVersion = LevelVersion.parse(dynamic);
            LevelSettings worldSettings = LevelSettings.parse(dynamic, mcServer.datapackconfiguration);

            worldDataServer = PrimaryLevelData.parse(dynamic, mcServer.getFixerUpper(), dataVersion, null, worldSettings,
                    levelVersion, serverProperties.getWorldGenSettings(mcServer.registryHolder), Lifecycle.stable());
        } else {
            Optional<NBTCompound> gameRules = Optional.ofNullable(extraData.getCompound("gamerules"));
            GameRules rules = new GameRules();

            gameRules.ifPresent(nbtCompound -> {
                CompoundTag compoundTag = (CompoundTag) Converter.convertToTag(nbtCompound);
                Map<String, GameRules.Key<?>> gameRuleKeys = CraftWorld.getGameRulesNMS();

                compoundTag.getAllKeys().forEach(gameRule -> {
                    if (gameRuleKeys.containsKey(gameRule)) {
                        GameRules.Value<?> gameRuleValue = rules.getRule(gameRuleKeys.get(gameRule));
                        String theValue = compoundTag.getString(gameRule);
                        gameRuleValue.deserialize(theValue);
                        gameRuleValue.onChanged(mcServer);
                    }
                });
            });

            LevelSettings worldSettings = new LevelSettings(worldName, serverProperties.gamemode, false,
                    serverProperties.difficulty, false, rules, mcServer.datapackconfiguration);

            worldDataServer = new PrimaryLevelData(worldSettings, serverProperties.getWorldGenSettings(mcServer.registryHolder), Lifecycle.stable());
        }

        worldDataServer.checkName(worldName);
        worldDataServer.setModdedInfo(mcServer.getServerModName(), mcServer.getModdedStatus().shouldReportAsModified());
        worldDataServer.setInitialized(true);

        return worldDataServer;
    }

    @Override
    public InfernalWorld getInfernalWorld(World world) {
        CraftWorld craftWorld = (CraftWorld) world;
        if (!(craftWorld.getHandle() instanceof InfernalWorldServer worldServer)) return null;
        return worldServer.getInfernalWorld();
    }

    @Override
    public void generateWorld(InfernalWorld world) {
        String worldName = world.getName();

        if (Bukkit.getWorld(worldName) != null) throw new IllegalArgumentException("World " + worldName + " already exists!");

        InfernalWorldServer worldServer = createCustomWorld(world, null);
        registerWorld(worldServer);
    }

    private void registerWorld(InfernalWorldServer worldServer) {
        MinecraftServer mcServer = MinecraftServer.getServer();
        mcServer.initWorld(worldServer, worldServer.serverLevelData, mcServer.getWorldData(), worldServer.serverLevelData.worldGenSettings());

        mcServer.levels.put(worldServer.dimension(), worldServer);
    }
}
