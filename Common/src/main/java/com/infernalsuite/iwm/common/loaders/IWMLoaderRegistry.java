package com.infernalsuite.iwm.common.loaders;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.loaders.LoaderRegistry;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
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

    @Override
    public @NonNull CompletableFuture<Optional<IWMLoader>> obtainLoader(@NonNull DataSource dataSource) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.eventDispatcher.dispatchPreObtainLoader(dataSource)) return Optional.empty();
            // TODO: Implement the actual matching of the DataSource to a loader
            return Optional.empty();
        });
    }

    @Override
    public @NonNull List<String> getRegisteredLoaders() {
        return this.loaderMap.keySet().stream().toList();
    }
}
