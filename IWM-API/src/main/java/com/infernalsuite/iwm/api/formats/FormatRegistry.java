package com.infernalsuite.iwm.api.formats;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Registry for available formats within this IWM instance.
 */
public interface FormatRegistry {

    /**
     * Register a {@link Format} with IWM.
     *
     * @param format the format to register
     */
    @NonNull CompletableFuture<Void> register(@NonNull Format format);

    /**
     * Gets a {@link Format} by its registered name.
     *
     * @param formatName the name of the format to retrieve
     * @return an {@link Optional} containing the format, or {@link Optional#empty()} if no registered format matches the given name
     */
    @NonNull CompletableFuture<Optional<Format>> getFormat(@NonNull String formatName);

    /**
     * Gets a {@link Format} by its registered name.
     *
     * @param formatName the name of the format to retrieve
     * @param executor the {@link Executor} to run the operation on
     * @return an {@link Optional} containing the format, or {@link Optional#empty()} if no registered format matches the given name
     */
    @NonNull CompletableFuture<Optional<Format>> getFormat(@NonNull String formatName, Executor executor);

    /**
     * Unregister the given {@link Format} from the {@link FormatRegistry registry}
     *
     * @param format the format to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull Format format);

    /**
     * Unregister a format using the format's given name.
     *
     * @param formatName the name of the format to unregister
     */
    @NonNull CompletableFuture<Void> unregister(@NonNull String formatName);

    /**
     * Gets a list of names of the formats registered with this registry.
     *
     * @return a list of registered formats names
     */
    @NonNull List<String> getRegisteredFormats();

}
