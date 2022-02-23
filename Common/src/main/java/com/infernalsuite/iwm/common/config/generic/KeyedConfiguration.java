package com.infernalsuite.iwm.common.config.generic;

import com.infernalsuite.iwm.common.config.generic.adapter.ConfigurationAdapter;
import com.infernalsuite.iwm.common.config.key.ConfigKey;
import com.infernalsuite.iwm.common.config.key.SimpleConfigKey;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyedConfiguration {

    private final ConfigurationAdapter adapter;
    private final List<? extends ConfigKey<?>> keys;
    private final ValuesMap values;

    public KeyedConfiguration(ConfigurationAdapter adapter, List<? extends ConfigKey<?>> keys) {
        this.adapter = adapter;
        this.keys = keys;
        this.values = new ValuesMap(keys.size());
    }

    protected void init() { load(true); }

    public <T> T get(ConfigKey<T> key) { return this.values.get(key); }

    protected void load(boolean initial) {
        for (ConfigKey<?> key : this.keys) {
            if (initial || key.reloadable()) {
                this.values.put(key, key.get(this.adapter));
            }
        }
    }

    public static List<SimpleConfigKey<?>> initialise(Class<?> keysClass) {
        List<SimpleConfigKey<?>> keys = Arrays.stream(keysClass.getFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> ConfigKey.class.equals(f.getType()))
                .map(f -> {
                    try {
                        return (SimpleConfigKey<?>) f.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).setOrdinal(i);
        }

        return keys;
    }

    public void reload() {
        this.adapter.reload();
        load(false);
    }

    public static class ValuesMap {
        private final Object[] values;

        public ValuesMap(int size) { this.values = new Object[size]; }

        @SuppressWarnings("unchecked")
        public <T> T get(ConfigKey<T> key) { return (T) this.values[key.ordinal()]; }

        public void put(ConfigKey<?> key, Object value) { this.values[key.ordinal()] = value; }

    }

}
