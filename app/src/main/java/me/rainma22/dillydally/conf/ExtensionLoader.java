package me.rainma22.dillydally.conf;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExtensionLoader {
    private URLClassLoader ucl;
    public ExtensionLoader(List<String> pathToJars) {
        URL[] urls = pathToJars.stream()
                .map(s -> Path.of(s))
                .filter(p -> Files.exists(p))
                .map(p -> {
                    try {
                        return p.toUri().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
        ucl = new URLClassLoader(urls);
    }

    public Class<?> load(String className) throws ClassNotFoundException{
        return ucl.loadClass(className);
    }
}
