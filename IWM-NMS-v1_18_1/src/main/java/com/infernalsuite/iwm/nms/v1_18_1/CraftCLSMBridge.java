package com.infernalsuite.iwm.nms.v1_18_1;

import com.infernalsuite.iwm.clsm.CLSMBridge;
import com.infernalsuite.iwm.clsm.ClassModifier;
import com.mojang.datafixers.util.Either;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CraftCLSMBridge implements CLSMBridge {

    private final v1181InfernalNMS nmsInstance;

    static void initialize(v1181InfernalNMS instance) {
        ClassModifier.setLoader(new CraftCLSMBridge(instance));
    }

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
    public Object[] getDefaultWorlds() {
        InfernalWorldServer defaultWorld = nmsInstance.getDefaultWorld();
        InfernalWorldServer netherworld = nmsInstance.getDefaultNetherWorld();
        InfernalWorldServer endWorld = nmsInstance.getDefaultEndWorld();

        if (defaultWorld != null || netherworld != null || endWorld != null) {
            return new InfernalWorldServer[]{defaultWorld, netherworld, endWorld};
        }

        return null;
    }

    @Override
    public boolean isCustomWorld(Object world) {
        return world instanceof InfernalWorldServer;
    }

    @Override
    public boolean skipWorldAdd(Object world) {
        if (!isCustomWorld(world) || nmsInstance.isLoadingDefaultWorlds()) return false;
        InfernalWorldServer worldServer = (InfernalWorldServer) world;
        return !worldServer.isReady();
    }

    @Override
    public Object getDefaultGamemode() {
        if (nmsInstance.isLoadingDefaultWorlds()) {
            return ((DedicatedServer) MinecraftServer.getServer()).getProperties().gamemode;
        }
        return null;
    }
}
