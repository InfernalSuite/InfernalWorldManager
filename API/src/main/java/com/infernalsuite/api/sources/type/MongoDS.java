package com.infernalsuite.api.sources.type;

import com.infernalsuite.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * MongoDB Data Source
 */
public interface MongoDS extends DataSource {

    @NonNull String getHostname();

    default int getPort() { return 27017; }

    default @NonNull String getDatabase() {
        return "infernalworldmanager";
    }

    default @NonNull String getCollection() {
        return "worlds";
    }

    @NonNull String getUsername();

    @NonNull String getPassword();

    default @NonNull String getAuthDB() {
        return "admin";
    }

}
