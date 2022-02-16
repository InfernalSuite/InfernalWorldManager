package com.infernalsuite.iwm.common.loaders.type;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.type.FileDS;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FileLoader implements IWMLoader {

    public static final String WORLD_FILE_EXTENSION = ".infernal";

    private static final FilenameFilter WORLD_FILE_FILTER = (d, n) -> n.endsWith(WORLD_FILE_EXTENSION);

    private final @NonNull String name;

    private @NonNull FileDS fileDataSource;

    private final @NonNull Map<String, RandomAccessFile> worldFiles = new ConcurrentHashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileLoader(@NotNull String name, @NotNull FileDS fileDataSource) {
        this.name = name;
        this.fileDataSource = fileDataSource;

        if (fileDataSource.getWorldDir().exists() && !fileDataSource.getWorldDir().isDirectory()) {
            // If a file exists instead of directory, delete it
            fileDataSource.getWorldDir().delete();
        }

        fileDataSource.getWorldDir().mkdirs();
    }

    @Override
    public @NonNull FileDS getDataSource() {
        return fileDataSource;
    }

    @Override
    public void setDataSource(@NonNull DataSource dataSource) {
        if (!(dataSource instanceof FileDS)) throw new IllegalArgumentException("Invalid data source type!");
        this.fileDataSource = (FileDS) dataSource;
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
        return new File(fileDataSource.getWorldDir(), worldName.concat(WORLD_FILE_EXTENSION)).exists();
    }

    @Override
    public @NonNull List<String> listWorlds() {
        String[] worlds = fileDataSource.getWorldDir().list(WORLD_FILE_FILTER);
        if (worlds == null) throw new IllegalStateException("Could not read worlds from world directory!");
        return Arrays.stream(worlds).map(w -> w.substring(0, w.length() - WORLD_FILE_EXTENSION.length())).collect(Collectors.toList());
    }

    @Override
    public void saveWorld(@NonNull String worldName, @NonNull SerializedDataWrapper world, boolean lock) {

    }

    @Override
    public void unlockWorld(@NonNull String worldName) throws IOException {
        RandomAccessFile file = worldFiles.remove(worldName);
        if (file != null) {
            FileChannel channel = file.getChannel();
            if (channel.isOpen()) file.close();
        }
    }

    @Override
    public boolean isWorldLocked(@NonNull String worldName) throws IOException {
        RandomAccessFile file = worldFiles.get(worldName);
        if (file == null) file = new RandomAccessFile(new File(fileDataSource.getWorldDir(), worldName.concat(WORLD_FILE_EXTENSION)), "rw");

        if (file.getChannel().isOpen()) {
            file.close();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void deleteWorld(@NonNull String worldName) {

    }
}
