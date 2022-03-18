package com.infernalsuite.iwm.common.api.implementation;

import com.infernalsuite.iwm.api.platform.Metadata;
import com.infernalsuite.iwm.api.platform.Platform;
import com.infernalsuite.iwm.common.environment.IwmEnv;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;

public class IwmPlatform implements Platform, Metadata {

    private final IwmEnv iwmEnv;

    public IwmPlatform(IwmEnv iwmEnv) { this.iwmEnv = iwmEnv; }

    @Override
    public @NonNull String getVersion() {
        return this.iwmEnv.getBootstrap().getVersion();
    }

    @Override
    public @NonNull String getApiVersion() {
        String[] version = this.iwmEnv.getBootstrap().getVersion().split("\\.");
        return version[0] + '.' + version[1];
    }

    @Override
    public @NonNull Type getType() {
        return this.iwmEnv.getBootstrap().getType();
    }

    @Override
    public @NonNull Instant getStartTime() {
        return this.iwmEnv.getBootstrap().getStartupTime();
    }

}
