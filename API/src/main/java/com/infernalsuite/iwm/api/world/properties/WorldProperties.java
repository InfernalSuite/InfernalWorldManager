package com.infernalsuite.iwm.api.world.properties;

import com.infernalsuite.iwm.api.world.properties.type.BooleanProperty;
import com.infernalsuite.iwm.api.world.properties.type.IntProperty;
import com.infernalsuite.iwm.api.world.properties.type.StringProperty;

public class WorldProperties {

    public static final WorldProperty<Integer> SPAWN_X = new IntProperty("spawnX", 0);

    public static final WorldProperty<Integer> SPAWN_Y = new IntProperty("spawnY", 255);

    public static final WorldProperty<Integer> SPAWN_Z = new IntProperty("spawnZ", 0);

    public static final WorldProperty<String> DIFFICULTY = new StringProperty("difficulty", "peaceful", value ->
            value.equalsIgnoreCase("peaceful") ||
                    value.equalsIgnoreCase("easy") ||
                    value.equalsIgnoreCase("normal") ||
                    value.equalsIgnoreCase("hard"));

    public static final WorldProperty<Boolean> ALLOW_MONSTERS = new BooleanProperty("allowMonsters", true);

    public static final WorldProperty<Boolean> ALLOW_ANIMALS = new BooleanProperty("allowAnimals", true);

    public static final WorldProperty<Boolean> DRAGON_BATTLE = new BooleanProperty("dragonBattle", false);

    public static final WorldProperty<Boolean> PVP = new BooleanProperty("pvp", true);

    public static final WorldProperty<String> ENVIRONMENT = new StringProperty("environment", "normal", value ->
            value.equalsIgnoreCase("normal") || value.equalsIgnoreCase("nether") || value.equalsIgnoreCase("the_end"));

    public static final WorldProperty<String> WORLD_TYPE = new StringProperty("worldtype", "default", value ->
            value.equalsIgnoreCase("default") || value.equalsIgnoreCase("flat") || value.equalsIgnoreCase("large_biomes")
                    || value.equalsIgnoreCase("amplified") || value.equalsIgnoreCase("customized")
                    || value.equalsIgnoreCase("debug_all_block_states") || value.equalsIgnoreCase("default_1_1")
    );

    public static final WorldProperty<String> DEFAULT_BIOME = new StringProperty("defaultBiome", "minecraft:plains");

}
