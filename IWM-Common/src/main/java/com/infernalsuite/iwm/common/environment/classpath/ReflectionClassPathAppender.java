package com.infernalsuite.iwm.common.environment.classpath;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ReflectionClassPathAppender implements ClassPathAppender {

    private final URLClassLoaderAccess classLoaderAccess;

    public ReflectionClassPathAppender(ClassLoader classLoader) throws IllegalStateException {
        if (classLoader instanceof URLClassLoader urlClassLoader) {
            this.classLoaderAccess = URLClassLoaderAccess.create(urlClassLoader);
        } else {
            throw new IllegalStateException("ClassLoader is not an instance of URLClassLoader");
        }
    }

    @Override
    public void AddJarToClasspath(Path file) {
        try {
            this.classLoaderAccess.addUrl(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
