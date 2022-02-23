package com.infernalsuite.iwm.common.config.generic.adapter;

import com.infernalsuite.iwm.common.environment.IwmEnv;

import java.util.List;
import java.util.Map;

public interface ConfigurationAdapter {

    IwmEnv getIwmEnv();

    void reload();

    String getString(String path, String def);

    int getInteger(String path, int def);

    boolean getBoolean(String path, boolean def);

    List<String> getStringList(String path, List<String> def);

    List<String> getKeys(String path, List<String> def);

    Map<String, String> getStringMap(String path, Map<String, String> def);

}
