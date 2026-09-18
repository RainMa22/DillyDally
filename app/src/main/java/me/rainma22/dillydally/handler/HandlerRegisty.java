package me.rainma22.dillydally.handler;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.sun.net.httpserver.HttpHandler;

public class HandlerRegisty {
    final Map<String, Function<Map<String, Object>, ? extends HttpHandler>> registeredHandlerConstructors = new HashMap<>(
            Map.of("FileHandler", (m) -> new FileHandler(Path.of(String.valueOf(m.get("directoryPath")))),
                    "PathRedirect", (m) -> {
                        try {
                            return new PathRedirectHandler(URI.create(String.valueOf(
                                    m.get("redirectTo"))).toURL(),
                                    Boolean.valueOf(String.valueOf(m.get("appendPath"))));
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    "ProtocolRedirect", (m) -> {
                        try {
                            Object o = m.get("port");
                            Integer port = null;
                            if (o != null) {
                                port = Integer.valueOf(String.valueOf(o));
                            }
                            return new ProtocolRedirectHandler(String.valueOf(m.get("protocol")), port);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));

    public void register(Class<? extends HttpHandler> clazz,
            Function<Map<String, Object>, ? extends HttpHandler> kwargConstructor) {
        registeredHandlerConstructors.put(clazz.getCanonicalName(), kwargConstructor);
    }

    public Optional<Function<Map<String, Object>, ? extends HttpHandler>> getConstructorOf(String name) {
        return Optional.ofNullable(registeredHandlerConstructors.getOrDefault(name, null));
    }

}
