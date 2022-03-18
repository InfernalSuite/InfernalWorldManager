package com.infernalsuite.iwm.common.environment.classpath;

import java.nio.file.Path;

public interface ClassPathAppender extends AutoCloseable {

    void AddJarToClasspath(Path file);

    @Override
    default void close() {}

}
