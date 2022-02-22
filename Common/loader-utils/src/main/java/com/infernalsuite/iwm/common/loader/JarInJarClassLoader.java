package com.infernalsuite.iwm.common.loader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class JarInJarClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public JarInJarClassLoader(ClassLoader loaderClassLoader, String jarResourcePath) throws LoadingException {
        super(new URL[]{ extractJar(loaderClassLoader, jarResourcePath) }, loaderClassLoader);
    }

    public void addJarToClasspath(URL url) { addURL(url); }

    public void deleteJarResource() {
        URL[] urls = getURLs();
        if (urls.length == 0) return;

        try {
            Path path = Paths.get(urls[0].toURI());
            Files.deleteIfExists(path);
        } catch (Exception ignored) {}

    }

    public <T> LoaderBootstrap instantiateIwmEnvironment(String bootstrapClass, Class<T> loaderType, T loader) throws LoadingException {
        Class<? extends LoaderBootstrap> environment;
        try {
            environment = loadClass(bootstrapClass).asSubclass(LoaderBootstrap.class);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to load boostrap class", e);
        }

        Constructor<? extends LoaderBootstrap> constructor;
        try {
            constructor = environment.getConstructor(loaderType);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to get bootstrap constructor", e);
        }

        try {
            return constructor.newInstance(loader);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to create bootstrap environment instance", e);
        }
    }

    private static URL extractJar(ClassLoader loaderClassLoader, String jarResourcePath) throws LoadingException {
        URL jarInJar = loaderClassLoader.getResource(jarResourcePath);
        if (jarInJar == null) throw new LoadingException("Could not locate jar-in-jar");

        Path path;
        try {
            path = Files.createTempFile("iwm-jarinjar", ".jar.tmp");
        } catch (IOException e) {
            throw new LoadingException("Unable to create a temporary file", e);
        }

        path.toFile().deleteOnExit();

        try (InputStream in = jarInJar.openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new LoadingException("Unable to copy jar-in-jar to temporary path", e);
        }

        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new LoadingException("Unable to get URl from path", e);
        }
    }

}
