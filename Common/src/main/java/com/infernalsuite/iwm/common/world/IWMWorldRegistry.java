package com.infernalsuite.iwm.common.world;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.WorldRegistry;
import com.infernalsuite.iwm.api.world.properties.WorldProperties;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IWMWorldRegistry implements WorldRegistry {

    private final IWMEventDispatcher eventDispatcher;

    public IWMWorldRegistry(IWMEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    private final Map<String, InfernalWorld> worldMap = new ConcurrentHashMap<>();

    @Override
    public @NonNull CompletableFuture<Void> register(@NonNull InfernalWorld world) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreRegisterWorld(world.getName(), world)) return;
            if (this.worldMap.putIfAbsent(world.getName(), world) == null) {
                this.eventDispatcher.dispatchPostRegisterWorld(world.getName(), world);
            }
        });
    }

    @Override
    public @NonNull CompletableFuture<Optional<InfernalWorld>> getWorld(@NonNull String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.eventDispatcher.dispatchPreGetWorld(worldName)) return Optional.empty();
            if (this.worldMap.containsKey(worldName)) {
                return Optional.ofNullable(this.eventDispatcher.dispatchPostGetWorld(worldName, worldMap.get(worldName)));
            } else return Optional.empty();
        });
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull InfernalWorld world) {
        return unregister(world.getName());
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull String worldName) {
        return CompletableFuture.runAsync(() -> {
           if (this.eventDispatcher.dispatchPreUnregisterWorld(worldName)) return;
           if (this.worldMap.remove(worldName) != null) {
               this.eventDispatcher.dispatchPostUnregisterWorld(worldName);
           }
        });
    }

    @Override
    public @NonNull List<String> getRegisteredWorlds() {
        return worldMap.keySet().stream().toList();
    }

    @Override
    public @NonNull CompletableFuture<Optional<InfernalWorld>> loadWorld(@NotNull IWMLoader loader, @NotNull String worldName, boolean readOnly, @NotNull WorldProperties properties) {
        return null;
    }

    @Override
    public @NonNull CompletableFuture<Optional<InfernalWorld>> createEmptyWorld(@NotNull String worldName, @NotNull IWMLoader loader, boolean readOnly, @NotNull WorldProperties properties) {
        return null;
    }

    @Override
    public @NonNull CompletableFuture<Void> migrateWorld(@NotNull String worldName, @NotNull IWMLoader currentLoader, @NotNull IWMLoader newLoader) {
        return null;
    }

    @Override
    public @NonNull CompletableFuture<Void> importWorld(@NotNull File worldDir, @NotNull String worldName, @NotNull IWMLoader loader) {
        return null;
    }

    @Override
    public void generateWorld(@NotNull InfernalWorld world) {

    }
}
