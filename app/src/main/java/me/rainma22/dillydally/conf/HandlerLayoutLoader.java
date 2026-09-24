package me.rainma22.dillydally.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpHandler;

import me.rainma22.dillydally.exceptions.InvalidLayoutException;
import me.rainma22.dillydally.handler.HandlerRegisty;

public final class HandlerLayoutLoader {
    private final HandlerRegisty registy;

    public HandlerLayoutLoader() {
        this(new HandlerRegisty());
    }

    public HandlerLayoutLoader(HandlerRegisty registy) {
        this.registy = registy;
    }

    public static final Map<String, Object> DEFAULT_LAYOUT = Map.of(
            "/", Map.of("FileHandler", new FileHandlerConfBean().toMap()));

    public Map<String, HttpHandler> fromJson(JSONObject json) throws InvalidLayoutException {
        var outMap = new HashMap<String, HttpHandler>();
        for (String k : json.keySet()) {
            JSONObject handlerCall = json.getJSONObject(k);
            if (handlerCall.keySet().size() != 1)
                throw new InvalidLayoutException(
                        "You cannot have more than 1 handler for any endpoint, " +
                                "including '" + k + "'!");
            for (String handlerName : handlerCall.keySet()) {
                HttpHandler handler = registy.getConstructorOf(handlerName)
                        .orElseThrow()
                        .apply(handlerCall.getJSONObject(handlerName).toMap());
                outMap.put(k, handler);
            }
        }
        return Collections.unmodifiableMap(outMap);
    }

}
