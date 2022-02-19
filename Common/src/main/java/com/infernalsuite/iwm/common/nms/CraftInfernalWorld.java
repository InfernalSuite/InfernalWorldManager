package com.infernalsuite.iwm.common.nms;

import com.infernalsuite.iwm.api.loaders.IWMLoader;
import com.infernalsuite.iwm.api.world.InfernalChunk;
import com.infernalsuite.iwm.api.world.InfernalWorld;
import com.infernalsuite.iwm.api.world.properties.WorldPropertyMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class CraftInfernalWorld implements InfernalWorld {

    private IWMLoader loader;
    private final String name;
    private final Map<Long, InfernalChunk> chunks;
    private final MutableNBTCompound extraData;
    private final List<NBTCompound> worldMaps;

    private byte version;

    private final WorldPropertyMap worldPropertyMap;

    private final boolean readOnly;
    private final boolean locked;

    private final String formatName;

    @Override
    public @NonNull InfernalChunk getChunk(int cx, int cz) {
        long index = calcIndex(cx, cz);
        synchronized (chunks) {
            return chunks.get(index);
        }
    }

    public void updateChunk(InfernalChunk chunk) {
        if (!chunk.getWorldName().equals(getName())) {
            // TODO: Detailed exception message
            throw new IllegalArgumentException("Chunk does not belong to this world!");
        }
        long index = calcIndex(chunk.getX(), chunk.getZ());
        synchronized (chunks) {
            chunks.put(index, chunk);
        }
    }

    public List<InfernalChunk> getChunkList() {
        List<InfernalChunk> sortedChunks;
        synchronized (chunks) {
            sortedChunks = new ArrayList<>(chunks.values());
        }
        sortedChunks.sort(Comparator.comparingLong(chunk -> calcIndex(chunk.getX(), chunk.getZ())));
        sortedChunks.removeIf(chunk -> chunk == null || Arrays.stream(chunk.getSections()).allMatch(Objects::isNull));

        return sortedChunks;
    }

    private static long calcIndex(int cx, int cz) {
        return ((long) cz) * Integer.MAX_VALUE + ((long) cx);
    }

}
