package com.infernalsuite.iwm.common.loaders.type;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.type.MySQLDS;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MySQLLoader implements IWMLoader {

    @Override
    public @NonNull MySQLDS getDataSource() {
        return null;
    }

    @Override
    public void setDataSource(@NonNull DataSource dataSource) {
        if (!(dataSource instanceof MySQLDS)) throw new IllegalArgumentException("Invalid data source type!");
    }

    @Override
    public @NonNull String getName() {
        return null;
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
    public @NotNull List<String> listWorlds() {
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
