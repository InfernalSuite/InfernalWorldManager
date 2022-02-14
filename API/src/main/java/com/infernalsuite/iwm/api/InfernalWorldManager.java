package com.infernalsuite.iwm.api;

import com.infernalsuite.iwm.api.event.EventBus;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.platform.Metadata;
import com.infernalsuite.iwm.api.platform.Platform;
import com.infernalsuite.iwm.api.platform.WorldAdapter;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface InfernalWorldManager {

    @NonNull WorldRegistry getWorldRegistry();

    @NonNull LoaderRegistry getLoaderRegistry();

    @NonNull EventBus getEventBus();

    <T> @NonNull WorldAdapter<T> getWorldAdapter(@NonNull Class<T> worldClass);

    @NonNull Platform getPlatform();

    @NonNull Metadata getMetadata();

}
