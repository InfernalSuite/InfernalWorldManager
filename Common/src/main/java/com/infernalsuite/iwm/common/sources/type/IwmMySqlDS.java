package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.MySQLDS;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
public class IwmMySqlDS implements MySQLDS {

    private final String name;
    private final boolean enabled;
    private final String sqlURL;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean useSSL;

    public IwmMySqlDS(String name, boolean enabled, String host, String username, String password) {
        this.name = name;
        this.enabled = enabled;
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = MySQLDS.super.getDatabase();
        this.sqlURL = MySQLDS.super.getSqlUrl();
        this.port = MySQLDS.super.getPort();
        this.useSSL = MySQLDS.super.useSSL();
    }

    public IwmMySqlDS(String name, boolean enabled, String sqlURL, String host, String portString, String database, String username, String password, String useSSLString) {
        this.name = name;
        this.enabled = enabled;
        this.sqlURL = sqlURL == null ? MySQLDS.super.getSqlUrl() : sqlURL;
        this.host = host;
        this.port = portString == null ? MySQLDS.super.getPort() : Integer.parseInt(portString);
        this.database = database == null ? MySQLDS.super.getDatabase() : database;
        this.username = username;
        this.password = password;
        this.useSSL = useSSLString == null ? MySQLDS.super.useSSL() : Boolean.parseBoolean(useSSLString);
    }

    public IwmMySqlDS(String name, boolean enabled, String sqlURL, String host, int port, String database, String username, String password, boolean useSSL) {
        this.name = name;
        this.enabled = enabled;
        this.sqlURL = sqlURL;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.useSSL = useSSL;
    }

}
