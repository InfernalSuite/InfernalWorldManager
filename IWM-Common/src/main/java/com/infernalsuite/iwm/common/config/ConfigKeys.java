package com.infernalsuite.iwm.common.config;

import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.common.config.generic.KeyedConfiguration;
import com.infernalsuite.iwm.common.config.key.ConfigKey;
import com.infernalsuite.iwm.common.config.key.SimpleConfigKey;

import java.util.List;

import static com.infernalsuite.iwm.common.config.key.ConfigKeyFactory.booleanKey;
import static com.infernalsuite.iwm.common.config.key.ConfigKeyFactory.stringKey;
import static com.infernalsuite.iwm.common.config.key.ConfigKeyFactory.key;

public final class ConfigKeys {
    private ConfigKeys() {}

    public static final ConfigKey<Boolean> FILE_SOURCE_ENABLED = booleanKey("sources.file.enabled", true);
    public static final ConfigKey<String> FILE_SOURCE_NAME = stringKey("sources.file.name", "file");
    public static final ConfigKey<String> FILE_SOURCE_PATH = stringKey("sources.file.path", "slime_worlds");

//    public static final ConfigKey<List<DataSource>> DATA_SOURCES = key({
//    });

    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(ConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() { return KEYS; }

}
