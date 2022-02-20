package com.infernalsuite.iwm.clsm;

import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

public class ClassModifier {

    public static final BooleanSupplier BOOLEAN_SUPPLIER = () -> true;

    private static CLSMBridge customLoader;

    public static void setLoader(CLSMBridge loader) { customLoader = loader; }

    public static CompletableFuture<Object> getFutureChunk(Object world, int cx, int cz) {
        if (customLoader == null || !isCustomWorld(world)) return null;
        return CompletableFuture.supplyAsync(() -> customLoader.getChunk(world, cx, cz));
    }

    public static boolean saveChunk(Object world, Object chunkAccess) {
        return customLoader != null && customLoader.saveChunk(world, chunkAccess);
    }

    public static boolean isCustomWorld(Object world) {
        return customLoader != null && customLoader.isCustomWorld(world);
    }

    public static boolean skipWorldAdd(Object world) {
        return customLoader != null && customLoader.skipWorldAdd(world);
    }

    public static Object[] getDefaultWorlds() { return customLoader != null ? customLoader.getDefaultWorlds() : null; }

    public static Object getDefaultGamemode() {
        return customLoader != null ? customLoader.getDefaultGamemode() : null;
    }

}
