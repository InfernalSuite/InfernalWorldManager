package com.infernalsuite.iwm.common.config.key;

import com.google.common.collect.ImmutableMap;
import com.infernalsuite.iwm.common.config.generic.adapter.ConfigurationAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public interface ConfigKeyFactory<T> {

    ConfigKeyFactory<Boolean> BOOLEAN = ConfigurationAdapter::getBoolean;
    ConfigKeyFactory<String> STRING = ConfigurationAdapter::getString;
    ConfigKeyFactory<String> LOWERCASE_STRING = (config, path, def) -> config.getString(path, def).toLowerCase(Locale.ROOT);
    ConfigKeyFactory<Map<String, String>> STRING_MAP = (config, path, def) -> ImmutableMap.copyOf(config.getStringMap(path, ImmutableMap.of()));

    static <T> SimpleConfigKey<T> key(Function<ConfigurationAdapter, T> function) {
        return new SimpleConfigKey<>(function);
    }

    static <T> SimpleConfigKey<T> notReloadable(SimpleConfigKey<T> key) {
        key.setReloadable(false);
        return key;
    }

    static SimpleConfigKey<Boolean> booleanKey(String path, boolean def) {
        return key(new Bound<>(BOOLEAN, path, def));
    }

    static SimpleConfigKey<String> stringKey(String path, String def) {
        return key(new Bound<>(STRING, path, def));
    }

    static SimpleConfigKey<String> lowercaseStringKey(String path, String def) {
        return key(new Bound<>(LOWERCASE_STRING, path, def));
    }

    static SimpleConfigKey<Map<String, String>> mapKey(String path) {
        return key(new Bound<>(STRING_MAP, path, null));
    }

    T getValue(ConfigurationAdapter config, String path, T def);

    @RequiredArgsConstructor
    class Bound<T> implements Function<ConfigurationAdapter, T> {
        private final ConfigKeyFactory<T> factory;
        private final String path;
        private final T def;

        @Override
        public T apply(ConfigurationAdapter adapter) {
            return this.factory.getValue(adapter, this.path, this.def);
        }
    }

}
