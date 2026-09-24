package me.rainma22.dillydally.conf;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.rainma22.dillydally.abstracts.DillyDallyExtension;
import me.rainma22.dillydally.exceptions.InvalidExtensionException;
import me.rainma22.dillydally.exceptions.InvalidNamespaceException;
import me.rainma22.dillydally.handler.HandlerRegisty;

public class ExtensionLoader implements AutoCloseable {
    private Logger LOGGER = LogManager.getLogger();
    private Map<String, URLClassLoader> classLoaderMap = new HashMap<>(2);

    public ExtensionLoader(Map<String, List<String>> pathToJars) {
        pathToJars.forEach((k, v) -> {
            var cl = new URLClassLoader(v.stream().map(s -> Path.of(s))
                    .peek(p -> {
                        if (!Files.exists(p))
                            LOGGER.warn("path " + p.toString() + " does not exists, skipping...");
                    })
                    .filter(p -> Files.exists(p))
                    .map(p -> {
                        try {
                            return p.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new), getClass().getClassLoader());
            classLoaderMap.put(k, cl);
        });
        if (!classLoaderMap.containsKey("default")) {
            classLoaderMap.put("default", new URLClassLoader(new URL[] {}, getClass().getClassLoader()));
        }
    }

    public void load(String className, HandlerRegisty hr)
            throws ClassNotFoundException, InvalidExtensionException, InvalidNamespaceException {
        String namespace = "default";
        int split = className.lastIndexOf("_");
        if (split != -1) {
            namespace = className.substring(0, split);
            className = className.substring(split + 1, className.length());
        }
        LOGGER.info("Loading extension '{}' from namespace '{}'", namespace, className);
        var ns = namespace;
        Class<?> clazz = Optional.ofNullable(classLoaderMap.getOrDefault(ns, null))
                .orElseThrow(() -> new InvalidNamespaceException("namespace: " + ns + " is invalid"))
                .loadClass(className);
        if (!DillyDallyExtension.class.isAssignableFrom(clazz)) {
            throw new InvalidExtensionException(clazz.getCanonicalName() + " is not an Extension");
        }
        try {
            DillyDallyExtension dde = (DillyDallyExtension) clazz.getConstructor().newInstance();
            dde.onLoad(hr);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InvalidExtensionException(
                    "Extension " + clazz.getCanonicalName() + " does not have a public zero-parameter constructor",
                    e);
        } catch (ClassCastException cce) {
            throw new InvalidExtensionException(
                    "Extension " + clazz.getCanonicalName() + " is not an Extension.",
                    cce);
        }
        LOGGER.info("Loaded extension '{}' from namespace '{}'", namespace, className);
    }

    @Override
    public void close() throws Exception {
        IOException ie = null;
        for (var entry : classLoaderMap.entrySet()) {
            var loader = entry.getValue();
            try {
                loader.close();
            } catch (IOException e) {
                e = new IOException("Error when trying to free namespace "
                        + entry.getKey() + ": ", e);
                if (ie == null)
                    ie = e;
                else
                    ie.addSuppressed(e);
            }
        }
        if (ie != null)
            throw ie;
    }
}
