package me.rainma22.dillydally.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpHandler;

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

    public Map<String, HttpHandler> fromJson(JSONObject json) {
        var outMap = new HashMap<String, HttpHandler>();
        for (String k : json.keySet()) {
            JSONObject handlerCall = json.getJSONObject(k);
            assert handlerCall.keySet().size() == 1;
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
