package com.infernalsuite.iwm.common.event;

import com.infernalsuite.iwm.api.event.EventDispatcher;
import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.events.format.*;
import com.infernalsuite.iwm.api.event.events.loaders.*;
import com.infernalsuite.iwm.api.event.events.sources.*;
import com.infernalsuite.iwm.api.event.events.world.*;
import com.infernalsuite.iwm.api.event.type.Cancellable;
import com.infernalsuite.iwm.api.event.type.ResultEvent;
import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import com.infernalsuite.iwm.common.event.gen.GeneratedEventClass;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class IWMEventDispatcher implements EventDispatcher {

    /**
     * The event bus being used to post events
     */
    private final AbstractEventBus<?> eventBus;

    public IWMEventDispatcher(AbstractEventBus<?> eventBus) { this.eventBus = eventBus; }

    /**
     * Gets the current event bus.
     *
     * @return the event bus
     */
    public AbstractEventBus<?> getEventBus() { return this.eventBus; }

    /**
     * Generate an {@link IWMEvent} for the given {@code event} type.
     *
     * @param eventClass the event type
     * @param params the event parameters
     * @return the generated event
     */
    private IWMEvent generate(Class<? extends IWMEvent> eventClass, Object... params) {
        try {
            return GeneratedEventClass.generate(eventClass).newInstance(this.eventBus.getApi(), params);
        } catch (Throwable e) {
            throw new RuntimeException("Exception occurred whilst generating event instance", e);
        }
    }

    /**
     * Posts an event to the event bus.
     *
     * @param eventClass the type of event to post
     * @param params the parameters of the event
     */
    private void post(Class<? extends IWMEvent> eventClass, Object... params) {
        IWMEvent event = generate(eventClass, params);
        this.eventBus.post(event);
    }

    public void postAsync(@NonNull Class<? extends IWMEvent> eventClass, Object... params) {
        if (Cancellable.class.isAssignableFrom(eventClass) || ResultEvent.class.isAssignableFrom(eventClass)) {
            throw new RuntimeException("Event cannot be posted async (" + eventClass.getName() + ")");
        }

        if (!this.eventBus.shouldPost(eventClass)) return;

        CompletableFuture.runAsync(() -> post(eventClass, params));
    }

    public void postSync(@NonNull Class<? extends IWMEvent> eventClass, Object... params) {
        if (!this.eventBus.shouldPost(eventClass)) return;
        post(eventClass, params);
    }

    public boolean postCancellable(@NonNull Class<? extends IWMEvent> eventClass, Object... params) {
        if (!Cancellable.class.isAssignableFrom(eventClass)) {
            throw new RuntimeException("Event is not cancellable: " + eventClass.getName());
        }

        boolean initialState = (boolean) params[0];

        if (!this.eventBus.shouldPost(eventClass)) return initialState;

        AtomicBoolean cancel = new AtomicBoolean(initialState);
        params[0] = cancel;
        post(eventClass, params);

        return cancel.get();
    }

    //region Known Event Dispatchers
    //=========================================================================

    /**
     * Dispatch a {@link PostGetFormatEvent}
     *
     * @param formatName the name of the format
     * @param initial the format which should be used as the initial value of {@link ResultEvent#result()}
     * @return the resulting {@link Format format} from the event
     */
    public Format dispatchPostGetFormat(String formatName, Format initial) {
        AtomicReference<Format> result = new AtomicReference<>(initial);
        postSync(PostGetFormatEvent.class, result, formatName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostRegisterFormatEvent}
     *
     * @param formatName the name of the format which was registered
     * @param format the format which was registered
     */
    public void dispatchPostRegisterFormat(String formatName, Format format) {
        postAsync(PostRegisterFormatEvent.class, formatName, format);
    }

    /**
     * Dispatch a {@link PostUnregisterFormatEvent}
     *
     * @param formatName the name of the format which was unregistered
     */
    public void dispatchPostUnregisterFormat(String formatName) {
        postAsync(PostUnregisterFormatEvent.class, formatName);
    }

    /**
     * Dispatch a {@link PreGetFormatEvent}
     *
     * @param formatName the name of the format being requested from the {@link FormatRegistry}
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreGetFormat(String formatName) {
        return postCancellable(PreGetFormatEvent.class, false, formatName);
    }

    /**
     * Dispatch a {@link PreRegisterFormatEvent}
     *
     * @param formatName the name of the format being registered
     * @param format the {@link Format} being registered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreRegisterFormat(String formatName, Format format) {
        return postCancellable(PreRegisterFormatEvent.class, false, formatName, format);
    }

    /**
     * Dispatch a {@link PreUnregisterFormatEvent}
     *
     * @param formatName the name of the format being unregistered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreUnregisterFormat(String formatName) {
        return postCancellable(PreUnregisterFormatEvent.class, false, formatName);
    }

    /**
     * Dispatch a {@link PostGetLoaderEvent}
     *
     * @param loaderName the name of the loader
     * @param initial the loader which should be used as the initial value of {@link ResultEvent#result()}
     * @return the resulting {@link IWMLoader loader} from the event
     */
    public IWMLoader dispatchPostGetLoader(String loaderName, IWMLoader initial) {
        AtomicReference<IWMLoader> result = new AtomicReference<>(initial);
        postSync(PostGetLoaderEvent.class, result, loaderName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostObtainLoaderEvent}
     *
     * @param dataSource the data source which was used to obtain the loader
     * @param initial the loader which should be used as the initial value of {@link ResultEvent#result()}
     * @return the resulting {@link IWMLoader loader} from the event
     */
    public IWMLoader dispatchPostObtainLoader(DataSource dataSource, IWMLoader initial) {
        AtomicReference<IWMLoader> result = new AtomicReference<>(initial);
        postSync(PostObtainLoaderEvent.class, result, dataSource);
        return result.get();
    }

    /**
     * Dispatch a {@link PostRegisterLoaderEvent}
     *
     * @param loaderName the name of the loader which was registered
     * @param loader the loader which was registered
     */
    public void dispatchPostRegisterLoader(String loaderName, IWMLoader loader) {
        postAsync(PostRegisterLoaderEvent.class, loaderName, loader);
    }

    /**
     * Dispatch a {@link PostUnregisterLoaderEvent}
     *
     * @param loaderName the name of the loader which was unregistered.
     */
    public void dispatchPostUnregisterLoader(String loaderName) {
        postAsync(PostUnregisterLoaderEvent.class, loaderName);
    }

    /**
     * Dispatch a {@link PreGetLoaderEvent}
     *
     * @param loaderName the name of the loader being requested from the {@link LoaderRegistry}
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreGetLoader(String loaderName) {
        return postCancellable(PreGetLoaderEvent.class, false, loaderName);
    }

    /**
     * Dispatch a {@link PreObtainLoaderEvent}
     *
     * @param dataSource the data source being used to obtain a {@link IWMLoader loader}
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreObtainLoader(DataSource dataSource) {
        return postCancellable(PreObtainLoaderEvent.class, false, dataSource);
    }

    /**
     * Dispatch a {@link PreRegisterLoaderEvent}
     *
     * @param loaderName the name of the loader being registered
     * @param loader the {@link IWMLoader loader} being registered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreRegisterLoader(String loaderName, IWMLoader loader) {
        return postCancellable(PreRegisterLoaderEvent.class, false, loaderName, loader);
    }

    /**
     * Dispatch a {@link PreUnregisterLoaderEvent}
     *
     * @param loaderName the name of the loader being unregistered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreUnregisterLoader(String loaderName) {
        return postCancellable(PreUnregisterLoaderEvent.class, false, loaderName);
    }

    /**
     * Dispatch a {@link PostGetDataSourceEvent}
     *
     * @param dataSourceName the name of the data source
     * @param initial the {@link DataSource data source} which should be used as the initial value of {@link ResultEvent#result()}
     * @return the resulting {@link DataSource data source} from the event
     */
    public DataSource dispatchPostGetDataSource(String dataSourceName, DataSource initial) {
        AtomicReference<DataSource> result = new AtomicReference<>(initial);
        postSync(PostGetDataSourceEvent.class, result, dataSourceName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostRegisterDataSourceEvent}
     *
     * @param dataSourceName the name of the data source which was registered
     * @param dataSource the data source which was registered
     */
    public void dispatchPostRegisterDataSource(String dataSourceName, DataSource dataSource) {
        postAsync(PostRegisterDataSourceEvent.class, dataSourceName, dataSource);
    }

    /**
     * Dispatch a {@link PostUnregisterDataSourceEvent}
     *
     * @param dataSourceName the name of the data source which was unregistered
     */
    public void dispatchPostUnregisterDataSource(String dataSourceName) {
        postAsync(PostUnregisterDataSourceEvent.class, dataSourceName);
    }

    /**
     * Dispatch a {@link PreGetDataSourceEvent}
     *
     * @param dataSourceName the name of the data source being requested from the {@link DataSourceRegistry}
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreGetDataSourceEvent(String dataSourceName) {
        return postCancellable(PreGetDataSourceEvent.class, false, dataSourceName);
    }

    /**
     * Dispatch a {@link PreRegisterDataSourceEvent}
     *
     * @param dataSourceName the name of the data source being registered
     * @param dataSource the {@link DataSource data source} being registered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreRegisterDataSource(String dataSourceName, DataSource dataSource) {
        return postCancellable(PreRegisterDataSourceEvent.class, false, dataSourceName, dataSource);
    }

    /**
     * Dispatch a {@link PreUnregisterDataSourceEvent}
     *
     * @param dataSourceName the name of the data source being unregistered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreUnregisterDataSource(String dataSourceName) {
        return postCancellable(PreUnregisterDataSourceEvent.class, false, dataSourceName);
    }

    /**
     * Dispatch a {@link PostCreateEmptyWorldEvent}
     *
     * @param worldName the name of the world which was created
     * @param initial the {@link InfernalWorld world} which should be used as the intial value of {@link ResultEvent#result()}
     * @return the resulting {@link InfernalWorld world} from the event
     */
    public InfernalWorld dispatchPostCreateEmptyWorld(String worldName, InfernalWorld initial) {
        AtomicReference<InfernalWorld> result = new AtomicReference<>(initial);
        postSync(PostCreateEmptyWorldEvent.class, result, worldName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostGenerateWorldEvent}
     *
     * @param worldName the name of the world which was generated
     * @param world the world which was generated
     */
    public void dispatchPostGenerateWorld(String worldName, InfernalWorld world) {
        postAsync(PostGenerateWorldEvent.class, worldName, world);
    }

    /**
     * Dispatch a {@link PostGetWorldEvent}
     *
     * @param worldName the name of the world
     * @param initial the {@link InfernalWorld world} which should be used as the initial value of {@link ResultEvent#getResult()}
     * @return the resulting {@link InfernalWorld world} from the event
     */
    public InfernalWorld dispatchPostGetWorld(String worldName, InfernalWorld initial) {
        AtomicReference<InfernalWorld> result = new AtomicReference<>(initial);
        postSync(PostGetWorldEvent.class, result, worldName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostImportWorldEvent}
     *
     * @param worldName the name of the world which was imported
     */
    public void dispatchPostImportWorld(String worldName) {
        postAsync(PostImportWorldEvent.class, worldName);
    }

    /**
     * Dispatch a {@link PostLoadWorldEvent}
     *
     * @param worldName the name of the world which has been loaded
     * @param initial the {@link InfernalWorld world} which should be used as the initial value of {@link ResultEvent#result()}
     * @return the resulting {@link InfernalWorld world} from the event
     */
    public InfernalWorld dispatchPostLoadWorld(String worldName, InfernalWorld initial) {
        AtomicReference<InfernalWorld> result = new AtomicReference<>(initial);
        postSync(PostLoadWorldEvent.class, result, worldName);
        return result.get();
    }

    /**
     * Dispatch a {@link PostMigrateWorldEvent}
     *
     * @param worldName the name of the world which was migrated
     * @param oldLoader the loader from which the world was migrated
     * @param newLoader the loader to which the world was migrated
     */
    public void dispatchPostMigrateWorld(String worldName, IWMLoader oldLoader, IWMLoader newLoader) {
        postAsync(PostMigrateWorldEvent.class, worldName, oldLoader, newLoader);
    }

    /**
     * Dispatch a {@link PostRegisterWorldEvent}
     *
     * @param worldName the name of the world which was registered
     * @param world the {@link InfernalWorld world} which was registered
     */
    public void dispatchPostRegisterWorld(String worldName, InfernalWorld world) {
        postAsync(PostRegisterWorldEvent.class, worldName, world);
    }

    /**
     * Dispatch a {@link PostUnregisterWorldEvent}
     *
     * @param worldName the name of the world which was unregistered
     */
    public void dispatchPostUnregisterWorld(String worldName) {
        postAsync(PostUnregisterWorldEvent.class, worldName);
    }

    /**
     * Dispatch a {@link PreCreateEmptyWorldEvent}
     *
     * @param worldName the name of the world being created
     * @param loader the loader being used for the world
     * @param readOnly whether the world is read only
     * @param worldPropertyMap the world properties
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreCreateEmptyWorld(String worldName, IWMLoader loader, boolean readOnly, WorldPropertyMap worldPropertyMap) {
        return postCancellable(PreCreateEmptyWorldEvent.class, false, worldName, loader, readOnly, worldPropertyMap);
    }

    /**
     * Dispatch a {@link PreGenerateWorldEvent}
     *
     * @param worldName the name of the world being generated
     * @param world the {@link InfernalWorld world} being generated
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreGenerateWorld(String worldName, InfernalWorld world) {
        return postCancellable(PreGenerateWorldEvent.class, false, worldName, world);
    }

    /**
     * Dispatch a {@link PreGetWorldEvent}
     *
     * @param worldName the name of the world being retrieved from the {@link WorldRegistry}
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreGetWorld(String worldName) {
        return postCancellable(PreGetWorldEvent.class, false, worldName);
    }

    /**
     * Dispatch a {@link PreImportWorldEvent}
     *
     * @param worldDir the world directory of the world being imported
     * @param worldName the name of the world being imported
     * @param loader the loader being used to import the world
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreImportWorld(File worldDir, String worldName, IWMLoader loader) {
        return postCancellable(PreImportWorldEvent.class, false, worldDir, worldName, loader);
    }

    /**
     * Dispatch a {@link PreLoadWorldEvent}
     *
     * @param loader the loader being used to load the world
     * @param worldName the name of the world being loaded
     * @param readOnly whether the world is read only
     * @param worldPropertyMap the world properties
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreLoadWorld(IWMLoader loader, String worldName, boolean readOnly, WorldPropertyMap worldPropertyMap) {
        return postCancellable(PreLoadWorldEvent.class, false, loader, worldName, readOnly, worldPropertyMap);
    }

    /**
     * Dispatch a {@link PreMigrateWorldEvent}
     *
     * @param worldName the name of the world being migrated
     * @param currentLoader the loader from which the world is being migrated
     * @param newLoader the loader to which the world is being migrated
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreMigrateWorld(String worldName, IWMLoader currentLoader, IWMLoader newLoader) {
        return postCancellable(PreMigrateWorldEvent.class, false, worldName, currentLoader, newLoader);
    }

    /**
     * Dispatch a {@link PreRegisterWorldEvent}
     *
     * @param worldName the name of the world being registered
     * @param world the {@link InfernalWorld world} being registered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreRegisterWorld(String worldName, InfernalWorld world) {
        return postCancellable(PreRegisterWorldEvent.class, false, worldName, world);
    }

    /**
     * Dispatch a {@link PreUnregisterWorldEvent}
     *
     * @param worldName the name of the world being unregistered
     * @return {@code true} if the event was cancelled
     */
    public boolean dispatchPreUnregisterWorld(String worldName) {
        return postCancellable(PreUnregisterWorldEvent.class, false, worldName);
    }

    //=========================================================================
    //endregion

    /**
     * Gets an array of all known event types at compile time.
     *
     * <p>Used to pre-generate the event classes for internal events for increased performance.</p>
     * @see GeneratedEventClass#preGenerate()
     *
     * @return an array of known events
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends IWMEvent>[] getKnownEventTypes() {
        return new Class[]{
                PostGetFormatEvent.class,
                PostRegisterFormatEvent.class,
                PostUnregisterFormatEvent.class,
                PreGetFormatEvent.class,
                PreRegisterFormatEvent.class,
                PreUnregisterFormatEvent.class,
                PostGetLoaderEvent.class,
                PostObtainLoaderEvent.class,
                PostRegisterLoaderEvent.class,
                PostUnregisterLoaderEvent.class,
                PreGetLoaderEvent.class,
                PreObtainLoaderEvent.class,
                PreRegisterLoaderEvent.class,
                PreUnregisterLoaderEvent.class,
                PostGetDataSourceEvent.class,
                PostRegisterDataSourceEvent.class,
                PostUnregisterDataSourceEvent.class,
                PreGetDataSourceEvent.class,
                PreRegisterDataSourceEvent.class,
                PreUnregisterDataSourceEvent.class,
                PostCreateEmptyWorldEvent.class,
                PostGenerateWorldEvent.class,
                PostGetWorldEvent.class,
                PostImportWorldEvent.class,
                PostLoadWorldEvent.class,
                PostMigrateWorldEvent.class,
                PostRegisterWorldEvent.class,
                PostUnregisterWorldEvent.class,
                PreCreateEmptyWorldEvent.class,
                PreGenerateWorldEvent.class,
                PreGetWorldEvent.class,
                PreImportWorldEvent.class,
                PreLoadWorldEvent.class,
                PreMigrateWorldEvent.class,
                PreRegisterWorldEvent.class,
                PreUnregisterWorldEvent.class
        };
    }

}
