package me.rainma22.dillydally.conf;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

import com.sun.net.httpserver.HttpHandler;

import me.rainma22.dillydally.abstracts.Bean;
import me.rainma22.dillydally.exceptions.InvalidExtensionException;
import me.rainma22.dillydally.handler.HandlerRegisty;

/**
 *
 */
public class ConfBean extends Bean {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String SELF_SIGN = "self-sign";
    private int httpPort = 80;
    private boolean doHttps = true;
    private int httpsPort = 443;
    private String serverUrl = SELF_SIGN;
    private Map<String, Object> layoutScheme = HandlerLayoutLoader.DEFAULT_LAYOUT;
    private Map<String, List<String>> extensionNameSpaces = Map.of("default", List.of());
    private List<String> enabledExtensions = List.of();

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private SSLCertificateConfBean sslCertificateConf = new SSLCertificateConfBean();

    private List<String> domains = List.of(
            "localhost",
            "127.0.0.1");

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public SSLCertificateConfBean getSslCertificateConf() {
        return sslCertificateConf;
    }

    public void setSslCertificateConf(SSLCertificateConfBean sslCertificateConf) {
        this.sslCertificateConf = sslCertificateConf;
    }

    public boolean isDoHttps() {
        return doHttps;
    }

    public void setDoHttps(boolean doHttps) {
        this.doHttps = doHttps;
    }

    public Map<String, Object> getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(Map<String, Object> layout) {
        this.layoutScheme = layout;
    }

    @JSONPropertyIgnore
    public Map<String, HttpHandler> getHandlerLayout() {
        HandlerRegisty registy = new HandlerRegisty();
        var extensionLoader = getExtensionLoader();
        for (String extensionName : enabledExtensions) {
            try {
                extensionLoader.load(extensionName, registy);
            } catch (ClassNotFoundException | InvalidExtensionException e) {
                LOGGER.error("Error when loading " + extensionName + ": ", e);
            }
        }
        return new HandlerLayoutLoader(registy).fromJson(new JSONObject(layoutScheme));
    }

    public static String getSelfSign() {
        return SELF_SIGN;
    }

    public Map<String, List<String>> getExtensionNameSpaces() {
        return extensionNameSpaces;
    }

    public void setExtensionNameSpaces(Map<String, List<String>> extensions) {
        this.extensionNameSpaces = extensions;
    }

    @JSONPropertyIgnore
    private ExtensionLoader getExtensionLoader() {
        return new ExtensionLoader(extensionNameSpaces);
    }

    public List<String> getEnabledExtensions() {
        return enabledExtensions;
    }

    public void setEnabledExtensions(List<String> enabledExtensions) {
        this.enabledExtensions = enabledExtensions;
    }
}
