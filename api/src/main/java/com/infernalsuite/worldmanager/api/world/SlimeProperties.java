package com.infernalsuite.worldmanager.api.world;

import com.infernalsuite.worldmanager.api.world.property.SlimePropertyIdentifier;

/**
 * Class with all existing slime world properties.
 */
public interface SlimeProperties {

    /**
     * The X coordinate of the world spawn
     *
     * Defaults to 0
     */
    SlimePropertyIdentifier<Integer> SPAWN_X = of("spawnX");

    /**
     * The Y coordinate of the world spawn
     *
     * Defaults to 255
     */
    SlimePropertyIdentifier<Integer> SPAWN_Y = of("spawnY");

    /**
     * The Z coordinate of the world spawn
     *
     * Defaults to 0
     */
    SlimePropertyIdentifier<Integer> SPAWN_Z = of("spawnZ");

    /**
     * The difficulty set for the world
     *
     * Defaults to "peaceful"
     * Allowed values are "peaceful", "easy", "normal", and "hard"
     */
    SlimePropertyIdentifier<String> DIFFICULTY = of("difficulty");

    /**
     * Whether monsters are allowed to spawn at night or in the dark
     *
     * Defaults to true
     */
    SlimePropertyIdentifier<Boolean> ALLOW_MONSTERS = of("allowMonsters");

    /**
     * Whether peaceful animals are allowed to spawn
     *
     * Defaults to true
     */
    SlimePropertyIdentifier<Boolean> ALLOW_ANIMALS = of("allowAnimals");

    /**
     * Whether the dragon battle should be enabled in end worlds
     *
     * Defaults to false
     */
    SlimePropertyIdentifier<Boolean> DRAGON_BATTLE = of("dragonBattle");

    /**
     * Whether PVP combat is allowed
     *
     * Defaults to true
     */
    SlimePropertyIdentifier<Boolean> PVP = of("pvp");

    /**
     * The environment of the world
     *
     * Defaults to "normal"
     * Allowed values are "normal", "nether", and "the_end"
     */
    SlimePropertyIdentifier<String> ENVIRONMENT = of("environment");

    /**
     * The default biome generated in empty chunks
     *
     * Defaults to "minecraft:plains"
     */
    SlimePropertyIdentifier<String> DEFAULT_BIOME = of("defaultBiome");

    private static <T> SlimePropertyIdentifier<T> of(String name) {
        return () -> name;
    }
}
