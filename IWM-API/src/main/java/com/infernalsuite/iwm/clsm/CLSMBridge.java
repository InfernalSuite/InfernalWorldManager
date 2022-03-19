package com.infernalsuite.iwm.clsm;

public interface CLSMBridge {

    /**
     * Returns a CompletableFuture that is Either.left(chunk). This can be completed async if needed.
     */
    default Object getChunk(Object world, int cx, int cz) { return null; }

    /**
     * Called when a chunk should be saved.
     *
     * @param world level
     * @param chunkAccess chunk
     * @return {@code true} to run custom saving behaviour, {@code false} to default to normal behaviour
     */
    default boolean saveChunk(Object world, Object chunkAccess) { return false; }

    /**
     * Checks if the world is a custom IWM world.
     *
     * @param world the world instance to check
     * @return {@code true} if a world is a custom IWM world
     */
    boolean isCustomWorld(Object world);

    /**
     * Called when the initial dimensions are being inserted into the game. This overrides the dimensions() method,
     * and the return value of this method will replace the value of dimensions(). Return null to return the normal
     * dimensions (overworld, nether, the_end). In the case of injecting, it should return an EMPTY MappedRegistry, and
     * then inject any custom worlds.
     * @return EMPTY MappedRegistry, or {@code null}
     */
    Object injectCustomWorlds();

}
