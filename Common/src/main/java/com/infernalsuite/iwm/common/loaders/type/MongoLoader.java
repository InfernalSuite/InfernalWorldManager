package com.infernalsuite.iwm.common.loaders.type;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.type.MongoDS;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class MongoLoader implements IWMLoader {

    private final @NonNull String name;

    private @NonNull MongoDS mongoDataSource;

    public MongoLoader(@NonNull String name, @NonNull MongoDS mongoDataSource) {
        this.name = name;
        this.mongoDataSource = mongoDataSource;
    }

    @Override
    public @NonNull MongoDS getDataSource() {
        return this.mongoDataSource;
    }

    @Override
    public void setDataSource(@NonNull DataSource dataSource) {
        if (!(dataSource instanceof MongoDS)) throw new IllegalArgumentException("Invalid data source type!");
        this.mongoDataSource = (MongoDS) dataSource;
    }

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public @NonNull SerializedDataWrapper loadWorld(@NonNull String worldName, boolean readOnly) {
        return null;
    }

    @Override
    public boolean worldExists(@NonNull String worldName) {
        return false;
    }

    @Override
    public @NonNull List<String> listWorlds() {
        return null;
    }

    @Override
    public void saveWorld(@NonNull String worldName, @NonNull SerializedDataWrapper world, boolean lock) {

    }

    @Override
    public void unlockWorld(@NonNull String worldName) {

    }

    @Override
    public boolean isWorldLocked(@NonNull String worldName) {
        return false;
    }

    @Override
    public void deleteWorld(@NonNull String worldName) {

    }
}
