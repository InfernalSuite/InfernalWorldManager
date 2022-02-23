package com.infernalsuite.iwm.common.config.key;

import com.infernalsuite.iwm.common.config.generic.adapter.ConfigurationAdapter;

public interface ConfigKey<T> {

    int ordinal();

    boolean reloadable();

    T get(ConfigurationAdapter adapter);

}
