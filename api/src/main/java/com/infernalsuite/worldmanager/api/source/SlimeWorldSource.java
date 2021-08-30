package com.infernalsuite.worldmanager.api.source;

import java.util.List;

public interface SlimeWorldSource extends SlimeWorldLoader, SlimeWorldSaveSource {

    boolean worldExists(String worldName);

    List<String> listWorlds();

}
