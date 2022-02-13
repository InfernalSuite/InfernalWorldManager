package com.infernalsuite.api.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides information about the platform IWM is running on.
 */
public interface Platform {

    /**
     * Gets the type of platform IWM is running on.
     *
     * @return the type of platform IWM is running on
     */
    Platform.@NonNull Type getType();

    /**
     * Represents a type of platform which IWM can run on.
     */
    @RequiredArgsConstructor
    enum Type {

        PAPER("Paper"),
        STANDALONE("Standalone");

        private final @NonNull @Getter String friendlyName;
    }

}
