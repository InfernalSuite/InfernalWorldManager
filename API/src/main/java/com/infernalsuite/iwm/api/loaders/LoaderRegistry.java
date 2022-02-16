package com.infernalsuite.iwm.api.loaders;

import com.infernalsuite.iwm.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Registry for available loaders within this IWM instance.
 */
public interface LoaderRegistry {

    /**
     * Register an {@link IWMLoader} with IWM.
     *
     * @param loader the loader to register
     * @param dataSource the data source the loader is using
     */
    @NonNull CompletableFuture<Void> register(@NonNull IWMLoader loader, @NonNull DataSource dataSource);

    /**
     * Gets an {@link IWMLoader} by its registered name.
     *
     * @param loaderName the name of the loader to retrieve
     * @return an {@link Optional} containing the loader, or {@link Optional#empty()} if no registered loader matches the given name
     */
    @NonNull CompletableFuture<Optional<IWMLoader>> getLoader(@NonNull String loaderName);

    /**
     * Unregister the given {@link IWMLoader loader} from the {@link LoaderRegistry registry}
     *
     * @param loader the loader to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull IWMLoader loader);

    /**
     * Unregister an {@link IWMLoader} using the loader's registered name
     *
     * @param loaderName the name of the loader to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull String loaderName);

    /**
     * Retrieve an {@link IWMLoader} which can process the given {@link DataSource}.
     *
     * @param dataSource the data source
     * @return an {@link Optional} containing the loader, or {@link Optional#empty()} if no loader is available that supports this data source
     */
    @NonNull CompletableFuture<Optional<IWMLoader>> obtainLoader(@NonNull DataSource dataSource);

    /**
     * Gets a list of the names of the loaders registered with this registry.
     *
     * @return a list of registered loaders names
     */
    @NonNull List<String> getRegisteredLoaders();

}
