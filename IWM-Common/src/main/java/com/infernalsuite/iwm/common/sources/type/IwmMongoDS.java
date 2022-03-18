package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.MongoDS;
import lombok.Getter;

@Getter
public class IwmMongoDS implements MongoDS {

    private final String name;
    private final boolean enabled;
    private final String hostname;
    private final int port;
    private final String database;
    private final String collection;
    private final String username;
    private final String password;
    private final String authDB;

    public IwmMongoDS(String name, boolean enabled, String hostname, String username, String password) {
        this.name = name;
        this.enabled = enabled;
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.port = MongoDS.super.getPort();
        this.database = MongoDS.super.getDatabase();
        this.collection = MongoDS.super.getCollection();
        this.authDB = MongoDS.super.getAuthDB();
    }

    public IwmMongoDS(String name, boolean enabled, String hostname, String port_string, String database, String collection, String username, String password, String authDB) {
        this.name = name;
        this.enabled = enabled;
        this.hostname = hostname;
        this.port = port_string == null ? MongoDS.super.getPort() : Integer.parseInt(port_string);
        this.database = database == null ? MongoDS.super.getDatabase() : database;
        this.collection = collection == null ? MongoDS.super.getCollection() : collection;
        this.username = username;
        this.password = password;
        this.authDB = authDB == null ? MongoDS.super.getAuthDB() : authDB;
    }

    public IwmMongoDS(String name, boolean enabled, String hostname, int port, String database, String collection, String username, String password, String authDB) {
        this.name = name;
        this.enabled = enabled;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.collection = collection;
        this.username = username;
        this.password = password;
        this.authDB = authDB;
    }

}
