package com.infernalsuite.iwm.nms.v1_18_2;

import com.infernalsuite.iwm.clsm.CLSMBridge;
import com.infernalsuite.iwm.clsm.ClassModifier;
import com.mojang.datafixers.util.Either;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CraftCLSMBridge implements CLSMBridge {

    private final v1182InfernalNMS nmsInstance;

    static void initialize(v1182InfernalNMS instance) { ClassModifier.setLoader(new CraftCLSMBridge(instance)); }

    @Override
    public Object getChunk(Object world, int cx, int cz) {
        InfernalWorldServer infernalWorld = (InfernalWorldServer) world;
        return Either.left(infernalWorld.getImposterChunk(cx, cz));
    }

    @Override
    public boolean saveChunk(Object world, Object chunkAccess) {
        if (!(world instanceof InfernalWorldServer infernalWorld)) return false;

        if (!(chunkAccess instanceof ImposterProtoChunk || chunkAccess instanceof LevelChunk) || !((ChunkAccess) chunkAccess).isUnsaved()) {
            return true;
        }

        LevelChunk chunk = chunkAccess instanceof ImposterProtoChunk
                ? ((ImposterProtoChunk) chunkAccess).getWrapped()
                : (LevelChunk) chunkAccess;

        infernalWorld.saveChunk(chunk);
        chunk.setUnsaved(false);

        return true;
    }

    @Override
    public boolean isCustomWorld(Object world) {
        return world instanceof InfernalWorldServer;
    }

    @Override
    public Object injectCustomWorlds() {
        return nmsInstance.injectDefaultWorlds();
    }

}
