package com.infernalsuite.iwm.common.environment;

import com.infernalsuite.iwm.api.InfernalWorldManager;
import com.infernalsuite.iwm.common.api.ApiRegistrationUtil;
import com.infernalsuite.iwm.common.api.IWMApi;
import com.infernalsuite.iwm.common.api.implementation.IwmWorldAdapter;
import com.infernalsuite.iwm.common.config.IwmConfiguration;
import com.infernalsuite.iwm.common.config.generic.adapter.ConfigurationAdapter;
import com.infernalsuite.iwm.common.environment.logging.IwmLogger;
import com.infernalsuite.iwm.common.event.AbstractEventBus;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import com.infernalsuite.iwm.common.event.gen.GeneratedEventClass;
import com.infernalsuite.iwm.common.formats.IWMFormatRegistry;
import com.infernalsuite.iwm.common.loaders.IWMLoaderRegistry;
import com.infernalsuite.iwm.common.sources.IWMDataSourceRegistry;
import com.infernalsuite.iwm.common.world.IWMWorldRegistry;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

public abstract class AbstractIwmEnv implements IwmEnv {

    private IWMApi iwmApi;
    private IWMWorldRegistry worldRegistry;
    private IWMLoaderRegistry loaderRegistry;
    private IWMDataSourceRegistry dataSourceRegistry;
    private IWMFormatRegistry formatRegistry;
    private IWMEventDispatcher eventDispatcher;
    private IwmWorldAdapter<?> iwmWorldAdapter;
    private IwmConfiguration configuration;

    public final void load() {

    }

    public final void enable() {

        getLogger().info("Loading configuration...");
        this.configuration = new IwmConfiguration(this, provideConfigurationAdapter());

        // Set up the IWM API
        this.iwmApi = new IWMApi(this);
        this.iwmApi.ensureApiWasLoadedByIwm();
        this.eventDispatcher = new IWMEventDispatcher(provideEventBus(this.iwmApi));
        getBootstrap().getScheduler().executeAsync(GeneratedEventClass::preGenerate);
        ApiRegistrationUtil.registerApi(this.iwmApi);
        registerApiOnPlatform(this.iwmApi);

        this.worldRegistry = new IWMWorldRegistry(this.eventDispatcher);
        this.loaderRegistry = new IWMLoaderRegistry(this.eventDispatcher);
        this.dataSourceRegistry = new IWMDataSourceRegistry(this.eventDispatcher);
        this.formatRegistry = new IWMFormatRegistry(this.eventDispatcher);

        this.iwmWorldAdapter = provideWorldAdapter();

        Duration timeTaken = Duration.between(getBootstrap().getStartupTime(), Instant.now());
        getLogger().info("Successfully enabled IWM in " + timeTaken.toMillis() + "ms.");

    }

    public final void disable() {

        getLogger().info("Starting shutdown process...");

        getBootstrap().getScheduler().shutdownScheduler();

        ApiRegistrationUtil.unregisterApi();

        getBootstrap().getScheduler().shutdownExecutor();

        getBootstrap().getClassPathAppender().close();

        getLogger().info("Goodbye!");
    }


    protected abstract AbstractEventBus<?> provideEventBus(IWMApi api);
    protected abstract void registerApiOnPlatform(InfernalWorldManager api);
    protected abstract <T> IwmWorldAdapter<T> provideWorldAdapter();
    protected abstract ConfigurationAdapter provideConfigurationAdapter();

    @Override
    public IWMWorldRegistry getWorldRegistry() {
        return this.worldRegistry;
    }

    @Override
    public IWMLoaderRegistry getLoaderRegistry() {
        return this.loaderRegistry;
    }

    @Override
    public IWMDataSourceRegistry getDataSourceRegistry() {
        return this.dataSourceRegistry;
    }

    @Override
    public IWMFormatRegistry getFormatRegistry() {
        return this.formatRegistry;
    }

    @Override
    public IWMEventDispatcher getEventDispatcher() {
        return this.eventDispatcher;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> IwmWorldAdapter<T> getWorldAdapter(Class<T> worldClass) {
        return (IwmWorldAdapter<T>) this.iwmWorldAdapter;
    }

    @Override
    public IwmConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public IwmLogger getLogger() {
        return getBootstrap().getIwmLogger();
    }

    @Override
    public IWMApi getApi() {
        return this.iwmApi;
    }

    public static String getEnvName() {
        LocalDate date = LocalDate.now();
        if (date.getMonth() == Month.APRIL && date.getDayOfMonth() == 1) {
            return "Some world management thing ¯\\_(ツ)_/¯";
        }
        return "InfernalWorldManager";
    }
}
