package com.infernalsuite.iwm.clsm;

public interface CLSMBridge {

    default Object getChunk(Object world, int cx, int cz) { return null; }

    default boolean saveChunk(Object world, Object chunkAccess) { return false; }

    Object[] getDefaultWorlds();

    boolean isCustomWorld(Object world);

    default boolean skipWorldAdd(Object world) { return false; }

    default Object getDefaultGamemode() { return null; }

}
