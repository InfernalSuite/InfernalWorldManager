package com.infernalsuite.api;

import com.infernalsuite.api.event.EventBus;
import com.infernalsuite.api.loaders.LoaderRegistry;
import com.infernalsuite.api.platform.Metadata;
import com.infernalsuite.api.platform.Platform;
import com.infernalsuite.api.platform.WorldAdapter;
import com.infernalsuite.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface InfernalWorldManager {

    @NonNull WorldRegistry getWorldRegistry();

    @NonNull LoaderRegistry getLoaderRegistry();

    @NonNull EventBus getEventBus();

    <T> @NonNull WorldAdapter<T> getWorldAdapter(@NonNull Class<T> worldClass);

    @NonNull Platform getPlatform();

    @NonNull Metadata getMetadata();

}
