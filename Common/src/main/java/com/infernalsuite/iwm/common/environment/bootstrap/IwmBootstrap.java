package com.infernalsuite.iwm.common.environment.bootstrap;

import com.infernalsuite.iwm.api.platform.Platform;
import com.infernalsuite.iwm.common.environment.classpath.ClassPathAppender;
import com.infernalsuite.iwm.common.environment.logging.IwmLogger;
import com.infernalsuite.iwm.common.environment.scheduler.SchedulerAdapter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

public interface IwmBootstrap {

    IwmLogger getIwmLogger();

    SchedulerAdapter getScheduler();

    ClassPathAppender getClassPathAppender();

    CountDownLatch getLoadLatch();

    CountDownLatch getEnableLatch();

    String getVersion();

    Instant getStartupTime();

    Platform.Type getType();

    String getServerVersion();

    Path getDataDirectory();

    default Path getConfigDirectory() { return getDataDirectory(); }

    default InputStream getResourceStream(String path) { return getClass().getClassLoader().getResourceAsStream(path); }

    default @Nullable String identifyClassLoader(ClassLoader classLoader) throws Exception {
        return null;
    }

}
