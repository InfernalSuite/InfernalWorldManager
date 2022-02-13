package com.infernalsuite.api.platform;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Provides information about IWM.
 */
public interface Metadata {

    /**
     * Gets the implementation version.
     *
     * @return the version of the implementation running on the platform
     */
    @NonNull String getVersion();

    /**
     * Gets the API version.
     *
     * @return the version of the API running on the platform
     */
    @NonNull String getApiVersion();

}
