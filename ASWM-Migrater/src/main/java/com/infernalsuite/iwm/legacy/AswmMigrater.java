package com.infernalsuite.iwm.legacy;

import com.infernalsuite.iwm.api.formats.SerializedDataWrapper;
import com.infernalsuite.iwm.api.sources.type.FileDS;
import com.infernalsuite.iwm.common.loaders.type.FileLoader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public final class AswmMigrater {

    public static void main(String[] args) {
        String worldName = args[0];
        System.out.println("Reading world: " + worldName);
        SerializedDataWrapper serializedDataWrapper = SlimeReader.readSlimeWorld(worldName);

        FileDS fileDS = new FileDS() {
            @Override
            public @NonNull File getWorldDir() {
                return new File("slime_worlds");
            }

            @Override
            public @NonNull String getName() {
                return "file";
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        FileLoader loader = new FileLoader("file_loader", fileDS);

        loader.saveWorld(worldName, serializedDataWrapper, false);
    }

}
