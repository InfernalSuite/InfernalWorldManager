package com.infernalsuite.iwm.common.sources;

import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.DataSourceRegistry;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IWMDataSourceRegistry implements DataSourceRegistry {

    private final IWMEventDispatcher eventDispatcher;

    public IWMDataSourceRegistry(IWMEventDispatcher eventDispatcher) { this.eventDispatcher = eventDispatcher; }

    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Override
    public @NonNull CompletableFuture<Void> register(@NonNull DataSource dataSource) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreRegisterDataSource(dataSource.getName(), dataSource)) return;
            if (this.dataSourceMap.putIfAbsent(dataSource.getName(), dataSource) == null) {
                // Only dispatch the event if we actually register a data source
                this.eventDispatcher.dispatchPostRegisterDataSource(dataSource.getName(), dataSource);
            }
        });
    }

    @Override
    public @NonNull CompletableFuture<Optional<DataSource>> getDataSource(@NonNull String dataSourceName) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.eventDispatcher.dispatchPreGetDataSourceEvent(dataSourceName)) return Optional.empty();
            if (this.dataSourceMap.containsKey(dataSourceName)) {
                return Optional.ofNullable(this.eventDispatcher.dispatchPostGetDataSource(dataSourceName, dataSourceMap.get(dataSourceName)));
            } else return Optional.empty();
        });
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull DataSource dataSource) {
        return unregister(dataSource.getName());
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull String dataSourceName) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreUnregisterDataSource(dataSourceName)) return;
            if (this.dataSourceMap.remove(dataSourceName) != null) {
                // Only dispatch the event if we actually unregister a data source
                this.eventDispatcher.dispatchPostUnregisterDataSource(dataSourceName);
            }
        });
    }

    @Override
    public @NonNull List<String> getRegisteredDataSources() {
        return this.dataSourceMap.keySet().stream().toList();
    }

}
