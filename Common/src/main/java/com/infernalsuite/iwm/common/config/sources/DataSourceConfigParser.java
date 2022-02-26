package com.infernalsuite.iwm.common.config.sources;

import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.common.sources.type.IwmFileDS;
import com.infernalsuite.iwm.common.sources.type.IwmMongoDS;
import com.infernalsuite.iwm.common.sources.type.IwmMySqlDS;

import java.io.File;
import java.util.Map;

public class DataSourceConfigParser {

    @SuppressWarnings("unchecked")
    public <T extends DataSource> T parse(String name, Map<String, String> config) throws NotParseable {
        String type = config.get("type");
        if (type == null) throw new NotParseable();
        switch (type.toLowerCase()) {
            case "file" -> {
                String path = config.get("path");
                String enabled_string = config.get("enabled");
                if (path == null || enabled_string == null) throw new NotParseable();
                IwmFileDS fileDS = new IwmFileDS(name, new File(path), Boolean.parseBoolean(enabled_string));
                return (T) fileDS;
            }
            case "mysql" -> {
                String host = config.get("host");
                String port_string = config.get("port");
                String database = config.get("database");
                String username = config.get("username");
                String password = config.get("password");
                String use_ssl_string = config.get("useSSL");
                String sql_url = config.get("sqlURL");
                String enabled_string = config.get("enabled");
                if (host == null || username == null || password == null || enabled_string == null) throw new NotParseable();
                IwmMySqlDS mySqlDS = new IwmMySqlDS(name, Boolean.parseBoolean(enabled_string), sql_url, host, port_string,
                        database, username, password, use_ssl_string);
                return (T) mySqlDS;
            }
            case "mongo" -> {
                String hostname = config.get("hostname");
                String port_string = config.get("port");
                String database = config.get("database");
                String collection = config.get("collection");
                String username = config.get("username");
                String password = config.get("password");
                String auth_db = config.get("authDB");
                String enabled_string = config.get("enabled");
                if (hostname == null || username == null || password == null || enabled_string == null) throw new NotParseable();
                IwmMongoDS mongoDS = new IwmMongoDS(name, Boolean.parseBoolean(enabled_string), hostname, port_string, database,
                        collection, username, password, auth_db);
                return (T) mongoDS;
            }
            // TODO: SeaweedFS
            // TODO: Couchbase
            default -> throw new NotParseable(); // If its not a natively supported data source, it can't be created from config!
        }
    }

    public static class NotParseable extends Exception {
        public NotParseable() { super(); }
    }

}
