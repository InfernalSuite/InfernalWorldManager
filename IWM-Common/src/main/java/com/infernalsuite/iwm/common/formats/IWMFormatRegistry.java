package com.infernalsuite.iwm.common.formats;

import com.infernalsuite.iwm.api.formats.Format;
import com.infernalsuite.iwm.api.formats.FormatRegistry;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IWMFormatRegistry implements FormatRegistry {

    private final IWMEventDispatcher eventDispatcher;

    public IWMFormatRegistry(IWMEventDispatcher eventDispatcher) { this.eventDispatcher = eventDispatcher; }

    private final Map<String, Format> formatMap = new ConcurrentHashMap<>();

    @Override
    public @NonNull CompletableFuture<Void> register(@NonNull Format format) {
        return CompletableFuture.runAsync(() -> {
            if (this.eventDispatcher.dispatchPreRegisterFormat(format.getName(), format)) return;
            if (this.formatMap.putIfAbsent(format.getName(), format) == null) {
                // Only dispatch the event if we actually register a format
                this.eventDispatcher.dispatchPostRegisterFormat(format.getName(), format);
            }
        });
    }

    @Override
    public @NonNull CompletableFuture<Optional<Format>> getFormat(@NonNull String formatName) {
        return CompletableFuture.supplyAsync(() -> {
           if (this.eventDispatcher.dispatchPreGetFormat(formatName)) return Optional.empty();
           if (this.formatMap.containsKey(formatName)) {
               return Optional.ofNullable(this.eventDispatcher.dispatchPostGetFormat(formatName, formatMap.get(formatName)));
           } else return Optional.empty();
        });
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull Format format) {
        return unregister(format.getName());
    }

    @Override
    public @NonNull CompletableFuture<Void> unregister(@NonNull String formatName) {
        return CompletableFuture.runAsync(() -> {
           if (this.eventDispatcher.dispatchPreUnregisterFormat(formatName)) return;
           if (this.formatMap.remove(formatName) != null) {
               // Only dispatch the event if we actually unregister a format
               this.eventDispatcher.dispatchPostUnregisterFormat(formatName);
           }
        });
    }

    @Override
    public @NonNull List<String> getRegisteredFormats() {
        return this.formatMap.keySet().stream().toList();
    }

}
