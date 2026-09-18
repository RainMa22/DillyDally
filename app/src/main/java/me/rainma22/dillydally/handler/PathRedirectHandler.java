package me.rainma22.dillydally.handler;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PathRedirectHandler implements HttpHandler {
    private URL redirectTo;
    private boolean appendPath;

    public PathRedirectHandler(URL redirectTo, boolean appendPath) {
        this.redirectTo = redirectTo;
        this.appendPath = appendPath;
    }

    @Override
    public void handle(HttpExchange exch) throws IOException {
        try {
            Path parent = Path.of(exch.getHttpContext().getPath());
            Path child = Path.of(exch.getRequestURI().getPath());
            Path rel = parent.relativize(child);
            exch.getResponseHeaders().add("Location",
                    appendPath ? new URL(redirectTo, rel.toString()).toString()
                            : redirectTo.toString());
            exch.sendResponseHeaders(307, -1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exch.close();
        }
    }
}
