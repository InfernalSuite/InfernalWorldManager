package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.MySQLDS;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IwmMySqlDS implements MySQLDS {

    @Override
    public @NonNull String getName() {
        return null;
    }

    @Override
    public @NonNull String getSqlUrl() {
        return MySQLDS.super.getSqlUrl();
    }

    @Override
    public @NonNull String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return MySQLDS.super.getPort();
    }

    @Override
    public @NonNull String getDatabase() {
        return MySQLDS.super.getDatabase();
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
    public boolean useSSL() {
        return MySQLDS.super.useSSL();
    }
}
