package me.rainma22.dillydally.conf;

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

import com.sun.jdi.ClassNotPreparedException;

import me.rainma22.dillydally.abstracts.DillyDallyExtension;
import me.rainma22.dillydally.exceptions.InvalidExtensionException;
import me.rainma22.dillydally.handler.HandlerRegisty;

public class ExtensionLoader {
    private Map<String, URLClassLoader> classLoaderMap = new HashMap<>(2);

    public ExtensionLoader(Map<String, List<String>> pathToJars) {
        pathToJars.forEach((k, v) -> {
            var cl = new URLClassLoader(v.stream().map(s -> Path.of(s))
                    .filter(p -> Files.exists(p))
                    .map(p -> {
                        try {
                            return p.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new));
            classLoaderMap.put(k, cl);
        });
        if(!classLoaderMap.containsKey("default")){
            classLoaderMap.put("default", new URLClassLoader(new URL[]{}));
        }
    }

    public void load(String className, HandlerRegisty hr) throws ClassNotFoundException, InvalidExtensionException {
        String namespace = "default";
        if (className.contains("_")) {
            var split = className.split("_");
            namespace = split[0];
            className = split[1];
        }
        var ns = namespace;
        Class<?> clazz = Optional.ofNullable(classLoaderMap.getOrDefault(ns, null))
                .orElseThrow(() -> new ClassNotPreparedException("namespace: " + ns + " is invalid"))
                .loadClass(className);
        if (!DillyDallyExtension.class.isAssignableFrom(clazz)) {
            throw new ClassNotFoundException(clazz.getCanonicalName() + " is not an Extension");
        }
        try {
            DillyDallyExtension dde = (DillyDallyExtension) clazz.getConstructor().newInstance();
            dde.onLoad(hr);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new InvalidExtensionException(
                    "Extension " + clazz.getCanonicalName() + " does not have a public zero-parameter constructor",
                    e);
        } catch (ClassCastException cce){
            throw new InvalidExtensionException(
                    "Extension " + clazz.getCanonicalName() + " is not an Extension.",
                    cce);
        }
    }
}
