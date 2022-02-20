package com.infernalsuite.iwm.common.loaders.type;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.sources.DataSource;
import com.infernalsuite.iwm.api.sources.type.FileDS;
import com.infernalsuite.iwm.common.formats.slime.SlimeFormatImpl;
import com.infernalsuite.iwm.common.formats.slime.SlimeSerializedDataWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
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
        if (!worldExists(worldName)) throw new IllegalStateException(worldName);
        RandomAccessFile file = worldFiles.computeIfAbsent(worldName, world -> {
            try {
                return new RandomAccessFile(new File(fileDataSource.getWorldDir(), worldName.concat(WORLD_FILE_EXTENSION)), "rw");
            } catch (FileNotFoundException ex) {
                return null;
            }
        });

        if (file != null) {
            try {
                file.seek(0);

                int formatBytesLength = file.readInt();
                byte[] formatBytes = new byte[formatBytesLength];
                file.read(formatBytes);

                switch (StandardCharsets.UTF_8.decode(ByteBuffer.wrap(formatBytes)).toString()) {
                    case "slime" -> {
                        SlimeSerializedDataWrapper serializedDataWrapper = new SlimeSerializedDataWrapper(
                                "slime",
                                worldName,
                                this,
                                null,
                                readOnly
                        );

                        serializedDataWrapper.setFormatBytesLength(formatBytesLength);
                        serializedDataWrapper.setFormatBytes(formatBytes);

                        byte[] slimeHeader = new byte[2];
                        file.read(slimeHeader);
                        if (!(slimeHeader == SlimeFormatImpl.SLIME_HEADER)) throw new IllegalStateException("Invalid Slime Header");
                        serializedDataWrapper.setSlimeHeader(slimeHeader);

                        serializedDataWrapper.setSlimeVersion(file.readByte());
                        serializedDataWrapper.setWorldVersion(file.readByte());

                        // Read World Info
                        serializedDataWrapper.setMinX(file.readInt());
                        serializedDataWrapper.setMinX(file.readInt());
                        int width = file.readInt();
                        int depth = file.readInt();
                        serializedDataWrapper.setWidth(width);
                        serializedDataWrapper.setDepth(depth);

                        // Read Chunk Data
                        int bitmaskSize = (int) Math.ceil((width * depth) / 8.0D);
                        byte[] chunkBitmask = new byte[bitmaskSize];
                        file.read(chunkBitmask);
                        serializedDataWrapper.setChunkBitmask(chunkBitmask);

                        int compressedChunkDataLength = file.readInt();
                        serializedDataWrapper.setCompressedChunkBytesLength(compressedChunkDataLength);
                        serializedDataWrapper.setChunkBytesLength(file.readInt());
                        byte[] compressedChunkData = new byte[compressedChunkDataLength];
                        file.read(compressedChunkData);
                        serializedDataWrapper.setChunkBytes(compressedChunkData);

                        int compressedTileEntitiesLength = file.readInt();
                        serializedDataWrapper.setCompressedTileEntitiesLength(compressedTileEntitiesLength);
                        serializedDataWrapper.setTileEntitiesLength(file.readInt());
                        byte[] compressedTileEntities = new byte[compressedTileEntitiesLength];
                        file.read(compressedTileEntities);
                        serializedDataWrapper.setTileEntities(compressedTileEntities);

                        boolean hasEntities = file.readBoolean();
                        serializedDataWrapper.setHasEntities(hasEntities);
                        if (hasEntities) {
                            int compressedEntitiesLength = file.readInt();
                            serializedDataWrapper.setCompressedEntitiesLength(compressedEntitiesLength);
                            serializedDataWrapper.setEntitiesLength(file.readInt());
                            byte[] compressedEntities = new byte[compressedEntitiesLength];
                            file.read(compressedEntities);
                            serializedDataWrapper.setEntities(compressedEntities);
                        }

                        int compressedNbtLength = file.readInt();
                        serializedDataWrapper.setCompressedNbtLength(compressedNbtLength);
                        serializedDataWrapper.setNbtLength(file.readInt());
                        byte[] compressedNbt = new byte[compressedNbtLength];
                        file.read(compressedNbt);
                        serializedDataWrapper.setNbtBytes(compressedNbt);

                        int compressedWorldMapsLength = file.readInt();
                        serializedDataWrapper.setCompressedMapsTagLength(compressedWorldMapsLength);
                        serializedDataWrapper.setMapsTagLength(file.readInt());
                        byte[] compressedWorldMaps = new byte[compressedWorldMapsLength];
                        file.read(compressedWorldMaps);
                        serializedDataWrapper.setMapsTag(compressedWorldMaps);

                        if (file.read() != -1) {
                            throw new IllegalStateException("Corrupted World!");
                        }

                        return serializedDataWrapper;
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

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
        RandomAccessFile file = worldFiles.get(worldName);
        boolean tempFile = file == null;

        SlimeSerializedDataWrapper serializedDataWrapper = (SlimeSerializedDataWrapper) world;

        try {
            if (tempFile) file = new RandomAccessFile(new File(fileDataSource.getWorldDir(), worldName.concat(WORLD_FILE_EXTENSION)), "rw");
            file.seek(0);
            file.setLength(0);

            file.writeInt(serializedDataWrapper.getFormatBytesLength());
            file.write(serializedDataWrapper.getFormatBytes());

            file.write(serializedDataWrapper.getSlimeHeader());
            file.writeByte(serializedDataWrapper.getSlimeVersion());
            file.writeByte(serializedDataWrapper.getWorldVersion());

            file.writeInt(serializedDataWrapper.getMinX());
            file.writeInt(serializedDataWrapper.getMinZ());
            file.writeInt(serializedDataWrapper.getWidth());
            file.writeInt(serializedDataWrapper.getDepth());

            file.write(serializedDataWrapper.getChunkBitmask());
            file.writeInt(serializedDataWrapper.getCompressedChunkBytesLength());
            file.writeInt(serializedDataWrapper.getChunkBytesLength());
            file.write(serializedDataWrapper.getChunkBytes());

            file.writeInt(serializedDataWrapper.getCompressedTileEntitiesLength());
            file.writeInt(serializedDataWrapper.getTileEntitiesLength());
            file.write(serializedDataWrapper.getTileEntities());

            boolean hasEntities = serializedDataWrapper.isHasEntities();
            if (hasEntities) {
                file.writeInt(serializedDataWrapper.getCompressedEntitiesLength());
                file.writeInt(serializedDataWrapper.getEntitiesLength());
                file.write(serializedDataWrapper.getEntities());
            }

            file.writeInt(serializedDataWrapper.getCompressedNbtLength());
            file.writeInt(serializedDataWrapper.getNbtLength());
            file.write(serializedDataWrapper.getNbtBytes());

            file.writeInt(serializedDataWrapper.getCompressedMapsTagLength());
            file.writeInt(serializedDataWrapper.getMapsTagLength());
            file.write(serializedDataWrapper.getMapsTag());

            if (lock) {
                FileChannel channel = file.getChannel();

                try {
                    channel.lock();
                } catch (OverlappingFileLockException ignored) {}
            }

            if (tempFile) file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
