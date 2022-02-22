package com.infernalsuite.iwm.common.environment.classpath;

import com.infernalsuite.iwm.common.loader.JarInJarClassLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public class JarInJarClassPathAppender implements ClassPathAppender {

    private final JarInJarClassLoader classLoader;

    public JarInJarClassPathAppender(ClassLoader classLoader) {
        if (!(classLoader instanceof JarInJarClassLoader jarInJarClassLoader)) {
            throw new IllegalArgumentException("Loader in not a Jar-In-Jar Class Loader: " + classLoader.getClass().getName());
        }
        this.classLoader = jarInJarClassLoader;
    }

    @Override
    public void AddJarToClasspath(Path file) {
        try {
            this.classLoader.addJarToClasspath(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.classLoader.deleteJarResource();
        try {
            this.classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
