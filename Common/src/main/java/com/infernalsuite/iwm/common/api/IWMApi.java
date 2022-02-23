package com.infernalsuite.iwm.common.api;

import com.infernalsuite.iwm.api.InfernalWorldManager;
import com.infernalsuite.iwm.api.event.EventBus;
import com.infernalsuite.iwm.api.event.EventDispatcher;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.platform.Metadata;
import com.infernalsuite.iwm.api.platform.Platform;
import com.infernalsuite.iwm.api.platform.WorldAdapter;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IWMApi implements InfernalWorldManager {

    @Override
    public @NonNull WorldRegistry getWorldRegistry() {
        return null;
    }

    @Override
    public @NonNull LoaderRegistry getLoaderRegistry() {
        return null;
    }

    @Override
    public @NonNull FormatRegistry getFormatRegistry() {
        return null;
    }

    @Override
    public @NonNull DataSourceRegistry getDataSourceRegistry() {
        return null;
    }

    @Override
    public @NonNull EventBus getEventBus() {
        return null;
    }

    @Override
    public @NonNull EventDispatcher getEventDispatcher() {
        return null;
    }

    @Override
    public @NonNull <T> WorldAdapter<T> getWorldAdapter(@NonNull Class<T> worldClass) {
        return null;
    }

    @Override
    public @NonNull Platform getPlatform() {
        return null;
    }

    @Override
    public @NonNull Metadata getMetadata() {
        return null;
    }
}
