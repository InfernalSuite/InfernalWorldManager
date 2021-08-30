package com.infernalsuite.worldmanager.api.source;

public interface SlimeWorldSaveSource {

    void saveWorld(String worldName, byte[] serializedWorld);

    void deleteWorld(String worldName);


}
