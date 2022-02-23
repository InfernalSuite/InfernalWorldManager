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
import com.infernalsuite.iwm.common.api.implementation.IwmPlatform;
import com.infernalsuite.iwm.common.environment.IwmEnv;
import com.infernalsuite.iwm.common.environment.bootstrap.BootstrappedWithLoader;
import com.infernalsuite.iwm.common.environment.bootstrap.IwmBootstrap;
import com.infernalsuite.iwm.common.environment.logging.IwmLogger;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IWMApi implements InfernalWorldManager {

    private final IwmEnv iwmEnv;

    private final IwmPlatform platform;

    public IWMApi(IwmEnv iwmEnv) {
        this.iwmEnv = iwmEnv;

        this.platform = new IwmPlatform(iwmEnv);
    }

    public void ensureApiWasLoadedByIwm() {
        IwmBootstrap bootstrap = this.iwmEnv.getBootstrap();
        ClassLoader iwmClassLoader;
        if (bootstrap instanceof BootstrappedWithLoader bootstrappedWithLoader) {
            iwmClassLoader = bootstrappedWithLoader.getLoader().getClass().getClassLoader();
        } else {
            iwmClassLoader = bootstrap.getClass().getClassLoader();
        }

        for (Class<?> apiClass : new Class[]{InfernalWorldManager.class, IWMApi.class}) {
            ClassLoader apiClassLoader = apiClass.getClassLoader();

            if (!apiClassLoader.equals(iwmClassLoader)) {
                String guilty = "unknown";
                try {
                    guilty = bootstrap.identifyClassLoader(apiClassLoader);
                } catch (Exception ignored) {}

                IwmLogger logger = this.iwmEnv.getLogger();
                logger.warn("The IWM API has not been loaded by IWM!");
                logger.warn("The API was loaded by " + apiClassLoader + " (" + guilty + ") and the " +
                        "IWM Environment was loaded by " + iwmClassLoader.toString() + ".");
                logger.warn("This probably means a plugin has incorrectly \"shaded\" the IWM API into its JAR file. This will cause" +
                        " errors at runtime and should be fixed.");
                return;
            }
        }
    }

    @Override
    public @NonNull WorldRegistry getWorldRegistry() {
        return this.iwmEnv.getWorldRegistry();
    }

    @Override
    public @NonNull LoaderRegistry getLoaderRegistry() {
        return this.iwmEnv.getLoaderRegistry();
    }

    @Override
    public @NonNull FormatRegistry getFormatRegistry() {
        return this.iwmEnv.getFormatRegistry();
    }

    @Override
    public @NonNull DataSourceRegistry getDataSourceRegistry() {
        return this.iwmEnv.getDataSourceRegistry();
    }

    @Override
    public @NonNull EventBus getEventBus() {
        return this.iwmEnv.getEventDispatcher().getEventBus();
    }

    @Override
    public @NonNull EventDispatcher getEventDispatcher() {
        return this.iwmEnv.getEventDispatcher();
    }

    @Override
    public @NonNull <T> WorldAdapter<T> getWorldAdapter(@NonNull Class<T> worldClass) {
        return this.iwmEnv.getWorldAdapter(worldClass);
    }

    @Override
    public @NonNull Platform getPlatform() {
        return this.platform;
    }

    @Override
    public @NonNull Metadata getMetadata() {
        return this.platform;
    }
}
