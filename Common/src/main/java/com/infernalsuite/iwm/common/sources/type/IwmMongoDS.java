package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.MongoDS;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IwmMongoDS implements MongoDS {

    @Override
    public @NonNull String getName() {
        return null;
    }

    @Override
    public @NonNull String getHostname() {
        return null;
    }

    @Override
    public int getPort() {
        return MongoDS.super.getPort();
    }

    @Override
    public @NonNull String getDatabase() {
        return MongoDS.super.getDatabase();
    }

    @Override
    public @NonNull String getCollection() {
        return MongoDS.super.getCollection();
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
    public @NonNull String getAuthDB() {
        return MongoDS.super.getAuthDB();
    }
}
