package com.infernalsuite.iwm.common.sources.type;

import com.infernalsuite.iwm.api.sources.type.FileDS;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

@RequiredArgsConstructor
public class IwmFileDS implements FileDS {

    private final String name;

    private final File worldDir;

    private final boolean enabled;

    @Override
    public @NonNull String getName() {
        return name;
    }

    @Override
    public @NonNull File getWorldDir() {
        return worldDir;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
