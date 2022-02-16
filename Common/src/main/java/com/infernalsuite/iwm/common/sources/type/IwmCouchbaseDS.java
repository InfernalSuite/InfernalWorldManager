package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.CouchbaseDS;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IwmCouchbaseDS implements CouchbaseDS {

    @Override
    public @NonNull String getName() {
        return null;
    }

    @Override
    public @NonNull String getHost() {
        return null;
    }

    @Override
    public @NonNull String getUsername() {
        return null;
    }

    @Override
    public @NonNull String getPassword() {
        return null;
    }

    @Override
    public @NonNull String getBucket() {
        return null;
    }

    @Override
    public String getCollection() {
        return CouchbaseDS.super.getCollection();
    }
}
