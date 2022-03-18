package com.infernalsuite.iwm.common.loaders;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.type.*;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import com.infernalsuite.iwm.common.loaders.type.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IWMLoaderRegistry implements LoaderRegistry {

    private final IWMEventDispatcher eventDispatcher;

    public IWMLoaderRegistry(IWMEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    private final Map<String, IWMLoader> loaderMap = new ConcurrentHashMap<>();

    @Override
    public @NonNull CompletableFuture<Void> register(@NonNull IWMLoader loader, @NonNull DataSource dataSource) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreRegisterLoader(loader.getName(), loader)) return;
            if (this.loaderMap.putIfAbsent(loader.getName(), loader) == null) {
                // Only dispatch the event if we actually registered a loader
                this.eventDispatcher.dispatchPostRegisterLoader(loader.getName(), loader);
            }
        });
    }

    @Override
    public @NonNull CompletableFuture<Optional<IWMLoader>> getLoader(@NonNull String loaderName) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.eventDispatcher.dispatchPreGetLoader(loaderName)) return Optional.empty();
            if (this.loaderMap.containsKey(loaderName)) {
                return Optional.ofNullable(this.eventDispatcher.dispatchPostGetLoader(loaderName, loaderMap.get(loaderName)));
            } else return Optional.empty();
        });
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull IWMLoader loader) {
        return unregister(loader.getName());
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull String loaderName) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreUnregisterLoader(loaderName)) return;
            if (this.loaderMap.remove(loaderName) != null) {
                // Only dispatch the event if we actually unregistered a loader
                this.eventDispatcher.dispatchPostUnregisterLoader(loaderName);
            }
        });
    }

    /**
     * @implNote This method performs no caching of the obtained loader against the data source used to obtain it.
     * This is by design, as a loader is bound to a data source. Loader names are auto-generated in the format using
     * {@link DataSource#getName()} and appending "_loader". This name is stored in the backing loader map to allow retrieval.
     * {@inheritDoc}
     */
    @Override
    public @NonNull CompletableFuture<Optional<IWMLoader>> obtainLoader(@NonNull DataSource dataSource) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.eventDispatcher.dispatchPreObtainLoader(dataSource)) return Optional.empty();
            String newLoaderName = dataSource.getName().concat("_loader");
            // TODO: Maybe we could change this to return the loader already in the map, but we first would need to validate the data source against the data source registry, to avoid non-deterministic behaviour
            if (this.loaderMap.containsKey(newLoaderName)) throw new IllegalStateException("Already registered loader clashes with generated name: " + newLoaderName);
            if (dataSource instanceof FileDS fileDataSource) {
                FileLoader loader = new FileLoader(newLoaderName, fileDataSource);
                if (!(this.eventDispatcher.dispatchPreRegisterLoader(newLoaderName, loader))) {
                    this.loaderMap.put(newLoaderName, loader);
                    this.eventDispatcher.dispatchPostRegisterLoader(newLoaderName, loader);
                }
                return Optional.ofNullable(this.eventDispatcher.dispatchPostObtainLoader(dataSource, loader));
            } else if (dataSource instanceof MySQLDS mySqlDataSource) {
                MySQLLoader loader = new MySQLLoader(newLoaderName, mySqlDataSource);
                if (!(this.eventDispatcher.dispatchPreRegisterLoader(newLoaderName, loader))) {
                    this.loaderMap.put(newLoaderName, loader);
                    this.eventDispatcher.dispatchPostRegisterLoader(newLoaderName, loader);
                }
                return Optional.ofNullable(this.eventDispatcher.dispatchPostObtainLoader(dataSource, loader));
            } else if (dataSource instanceof MongoDS mongoDataSource) {
                MongoLoader loader = new MongoLoader(newLoaderName, mongoDataSource);
                if (!(this.eventDispatcher.dispatchPreRegisterLoader(newLoaderName, loader))) {
                    this.loaderMap.put(newLoaderName, loader);
                    this.eventDispatcher.dispatchPostRegisterLoader(newLoaderName, loader);
                }
                return Optional.ofNullable(this.eventDispatcher.dispatchPostObtainLoader(dataSource, loader));
            } else if (dataSource instanceof SeaweedDS seaweedDataSource) {
                SeaweedLoader loader = new SeaweedLoader(newLoaderName, seaweedDataSource);
                if (!(this.eventDispatcher.dispatchPreRegisterLoader(newLoaderName, loader))) {
                    this.loaderMap.put(newLoaderName, loader);
                    this.eventDispatcher.dispatchPostRegisterLoader(newLoaderName, loader);
                }
                return Optional.ofNullable(this.eventDispatcher.dispatchPostObtainLoader(dataSource, loader));
            } else if (dataSource instanceof CouchbaseDS couchbaseDataSource) {
                CouchbaseLoader loader = new CouchbaseLoader(newLoaderName, couchbaseDataSource);
                if (!(this.eventDispatcher.dispatchPreRegisterLoader(newLoaderName, loader))) {
                    this.loaderMap.put(newLoaderName, loader);
                    this.eventDispatcher.dispatchPostRegisterLoader(newLoaderName, loader);
                }
                return Optional.ofNullable(this.eventDispatcher.dispatchPostObtainLoader(dataSource, loader));
            }
            return Optional.empty();
        });
    }

    @Override
    public @NonNull List<String> getRegisteredLoaders() {
        return this.loaderMap.keySet().stream().toList();
    }
}
