package com.infernalsuite.api.sources.type;

import com.infernalsuite.api.sources.DataSource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * MySQL Data Source
 */
public interface MySQLDS extends DataSource {

    default @NonNull String getSqlUrl() {
        return "jdbc:mysql://{host}:{port}/{database}?autoReconnect=true&allowMultiQueries=true&useSSL={useSSL}";
    }

    @NonNull String getHost();

    default int getPort() { return 3306; }

    default @NonNull String getDatabase() {
        return "infernalworldmanager";
    }

    @NonNull String getUsername();

    @NonNull String getPassword();

    default boolean useSSL() { return false; }

}
