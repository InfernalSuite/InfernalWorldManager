package com.infernalsuite.iwm.api;

import com.infernalsuite.iwm.api.event.EventBus;
import com.infernalsuite.iwm.api.event.EventDispatcher;
import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.platform.Metadata;
import com.infernalsuite.iwm.api.platform.Platform;
import com.infernalsuite.iwm.api.platform.WorldAdapter;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The IWM API.
 *
 * <p>The API allows other plugins or services to interact with IWM, such as listening to events, changing IWM's
 * behaviour, and integrating IWM into other systems.</p>
 *
 * <p>This interface represents the base of the API package. All API functions are accessed via this interface.</p>
 *
 * <p>To use the API, you need to obtain an instance of this interface. These are registered by IWM to the platform's
 * Services Manager. This is the preferred method for obtaining an instance.</p>
 *
 * <p>For ease of use, and for platforms without a Services Manager, an instance can also be obtained from the static
 * singleton accessor in {@link IWMProvider}</p>
 */
public interface InfernalWorldManager {

    /**
     * Gets the {@link WorldRegistry}, responsible for managing {@link InfernalWorld} instances.
     *
     * <p>This registry can be used to retrieve instances of {@link InfernalWorld} by name, registering worlds
     * with IWM, importing worlds into IWM, etc.</p>
     *
     * @return the world registry
     */
    @NonNull WorldRegistry getWorldRegistry();

    /**
     * Gets the {@link LoaderRegistry}, responsible for managing {@link IWMLoader} instances.
     *
     * <p>This registry can be used to retrieve instances of {@link IWMLoader} by name, registering new loaders
     * with IWM, obtaining a loader for a given {@link DataSource}, etc.</p>
     *
     * @return the loader registry
     */
    @NonNull LoaderRegistry getLoaderRegistry();

    /**
     * Gets the {@link FormatRegistry}, responsible for managing {@link Format} instances.
     *
     * <p>This registry can be used to retrieve instances of {@link Format} by name, registering new
     * formats with IWM, etc.</p>
     *
     * @return the format registry
     */
    @NonNull FormatRegistry getFormatRegistry();

    /**
     * Gets the {@link DataSourceRegistry}, responsible for managing {@link DataSource} instances.
     *
     * <p>This registry can be used to retrieve instances of {@link DataSource} by name, registering new
     * data sources with IWM, etc.</p>
     *
     * @return the data source registry
     */
    @NonNull DataSourceRegistry getDataSourceRegistry();


    /**
     * Gets the {@link EventBus}, used for subscribing to IWM events.
     *
     * @return the event bus
     */
    @NonNull EventBus getEventBus();

    /**
     * Gets the {@link EventDispatcher}, responsible for dispatching events in IWM.
     *
     * <p>The event dispatcher can be used to dispatch custom events for add-ons to IWM, which can be received by
     * subscribers registered with the {@link EventBus}.</p>
     *
     * @return the event dispatcher
     */
    @NonNull EventDispatcher getEventDispatcher();

    /**
     * Gets the {@link WorldAdapter}, a utility class for adapting {@link InfernalWorld} instances to platform world
     * objects, and vice versa.
     *
     * <p>The worldClass parameter must be equal to the class or interface used by the platform to represent worlds.</p>
     *
     * <p>E.g. for Bukkit, this should be {@code "org.bukkit.World"}</p>
     *
     * @throws IllegalArgumentException if the world class is incorrect
     *
     * @param worldClass the class used by the platform to represent worlds
     * @param <T> the world class type
     * @return the world adapter
     */
    <T> @NonNull WorldAdapter<T> getWorldAdapter(@NonNull Class<T> worldClass);

    /**
     * Gets the {@link Platform} which represents the platform IWM is running on.
     *
     * @return the platform
     */
    @NonNull Platform getPlatform();

    /**
     * Gets the {@link Metadata}, responsible for providing information about the IWM instance currently running.
     *
     * @return the metadata
     */
    @NonNull Metadata getMetadata();

}
