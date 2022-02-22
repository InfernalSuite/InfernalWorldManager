package com.infernalsuite.iwm.nms.v1_18_1;

import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import com.infernalsuite.iwm.common.nms.CraftInfernalWorld;
import com.infernalsuite.iwm.common.nms.InfernalNMS;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import lombok.Getter;
import net.minecraft.SharedConstants;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelVersion;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class v1181InfernalNMS implements InfernalNMS<World> {

    private static final Logger LOGGER = LogManager.getLogger("IWM");
    private static final File UNIVERSE_DIR;
    public static LevelStorageSource CONVERTABLE;
    public static boolean isPaperMC;

    static {
        Path path;

        try {
            path = Files.createTempDirectory("iwm-" + UUID.randomUUID().toString().substring(0, 5) + "-");
        } catch (IOException e) {
            path = null;
            System.exit(1);
        }

        UNIVERSE_DIR = path.toFile();
        CONVERTABLE = LevelStorageSource.createDefault(path);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDirectory(UNIVERSE_DIR);
            } catch (IOException ignored) {}
        }));
    }

    @Getter
    private final byte worldVersion = 0x08;

    @Getter
    private boolean loadingDefaultWorlds = true;

    private final FormatRegistry formatRegistry;

    private InfernalWorldServer defaultWorld;
    private InfernalWorldServer defaultNetherWorld;
    private InfernalWorldServer defaultEndWorld;

    public v1181InfernalNMS(FormatRegistry formatRegistry, boolean isPaper) {
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
    public void setDefaultWorlds(InfernalWorld normalWorld, InfernalWorld netherWorld, InfernalWorld endWorld) throws IOException {
        if (normalWorld != null) defaultWorld = createDefaultWorld(formatRegistry, normalWorld, LevelStem.OVERWORLD, ServerLevel.OVERWORLD);
        if (netherWorld != null) defaultNetherWorld = createDefaultWorld(formatRegistry, netherWorld, LevelStem.NETHER, ServerLevel.NETHER);
        if (endWorld != null) defaultEndWorld = createDefaultWorld(formatRegistry, endWorld, LevelStem.END, ServerLevel.END);

        loadingDefaultWorlds = false;
    }

    private InfernalWorldServer createDefaultWorld(FormatRegistry formatRegistry, InfernalWorld world, ResourceKey<LevelStem> dimensionKey, ResourceKey<Level> worldKey) {
        PrimaryLevelData worldDataServer = createWorldData(world);

        MappedRegistry<LevelStem> registryMaterials = worldDataServer.worldGenSettings().dimensions();
        LevelStem worldDimension = registryMaterials.get(dimensionKey);
        DimensionType dimensionManager = worldDimension.type();
        ChunkGenerator chunkGenerator = worldDimension.generator();

        World.Environment environment = getEnvironment(world);

        try {
            return new InfernalWorldServer((CraftInfernalWorld) world, formatRegistry, worldDataServer, worldKey, dimensionKey, dimensionManager, chunkGenerator, environment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

            worldDataServer = PrimaryLevelData.parse(dynamic, mcServer.getFixerUpper(), dataVersion, null, worldSettings, levelVersion,
                    serverProperties.getWorldGenSettings(mcServer.registryHolder), Lifecycle.stable());
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

            LevelSettings worldSettings = new LevelSettings(worldName, serverProperties.gamemode, false, serverProperties.difficulty,
                    false, rules, mcServer.datapackconfiguration);

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
    public void generateWorld(InfernalWorld infernalWorld) {
        String worldName = infernalWorld.getName();

        if (Bukkit.getWorld(worldName) != null) {
            throw new IllegalArgumentException("World " + worldName + " already exists! Cannot generate it...");
        }

        PrimaryLevelData worldDataServer = createWorldData(infernalWorld);
        World.Environment environment = getEnvironment(infernalWorld);
        ResourceKey<LevelStem> dimension;

        switch (environment) {
            case NORMAL -> dimension = LevelStem.OVERWORLD;
            case NETHER -> dimension = LevelStem.NETHER;
            case THE_END -> dimension = LevelStem.END;
            default -> throw new IllegalArgumentException("Unknown Dimension Supplied");
        }

        MappedRegistry<LevelStem> materials = worldDataServer.worldGenSettings().dimensions();
        LevelStem worldDimension = materials.get(dimension);
        DimensionType dimensionManager = worldDimension.type();
        ChunkGenerator chunkGenerator = worldDimension.generator();

        ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY,
                new ResourceLocation(worldName.toUpperCase(Locale.ENGLISH)));

        InfernalWorldServer worldServer;

        try {
            worldServer = new InfernalWorldServer((CraftInfernalWorld) infernalWorld, formatRegistry, worldDataServer, worldKey,
                    dimension, dimensionManager, chunkGenerator, environment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EndDragonFight dragonBattle = worldServer.dragonFight();
        boolean runBattle = infernalWorld.getWorldPropertyMap().getValue(WorldProperties.DRAGON_BATTLE);

        if (dragonBattle != null && !runBattle) {
            dragonBattle.dragonEvent.setVisible(false);
        }

        worldServer.setReady(true);

        MinecraftServer mcServer = MinecraftServer.getServer();
        mcServer.initWorld(worldServer, worldDataServer, mcServer.getWorldData(), worldDataServer.worldGenSettings());

        mcServer.levels.put(worldKey, worldServer);

        worldServer.setSpawnSettings(infernalWorld.getWorldPropertyMap().getValue(WorldProperties.ALLOW_MONSTERS),
                infernalWorld.getWorldPropertyMap().getValue(WorldProperties.ALLOW_ANIMALS));

        Bukkit.getPluginManager().callEvent(new WorldInitEvent(worldServer.getWorld()));
        Bukkit.getPluginManager().callEvent(new WorldLoadEvent(worldServer.getWorld()));
    }

    @Override
    public NBTCompound convertChunk(NBTCompound chunkTag) {
        CompoundTag nmsTag = (CompoundTag) Converter.convertToTag(chunkTag);
        int version = nmsTag.getInt("DataVersion");

        CompoundTag newNmsTag = NbtUtils.update(DataFixers.getDataFixer(), DataFixTypes.CHUNK, nmsTag, version);

        return (NBTCompound) Converter.convertToNBT(newNmsTag);
    }
}
