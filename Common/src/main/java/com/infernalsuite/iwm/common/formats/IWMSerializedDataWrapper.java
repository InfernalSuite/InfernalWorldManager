package com.infernalsuite.iwm.common.formats;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.common.utils.Table;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IWMSerializedDataWrapper implements SerializedDataWrapper {

    private final String formatName;

    private final Table<Integer, Integer, Byte[]> backingTable = new Table<>();

    @Override
    public Byte[] getChunkByteArray(int x, int z) {
        return backingTable.get(x, z);
    }

    @Override
    public void putChunkByteArray(int x, int z, Byte[] serializedChunk) {
        backingTable.put(x, z, serializedChunk);
    }

    @Override
    public String getFormatName() {
        return formatName;
    }

}
