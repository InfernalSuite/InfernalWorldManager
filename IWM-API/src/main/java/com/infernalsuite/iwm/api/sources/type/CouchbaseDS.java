package com.infernalsuite.iwm.api.sources.type;

import com.infernalsuite.iwm.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Couchbase Data Source
 */
public interface CouchbaseDS extends DataSource {

    @NonNull String getHost();

    @NonNull String getUsername();

    @NonNull String getPassword();

    @NonNull String getBucket();

    default String getCollection() {
        return "worlds";
    }

}
