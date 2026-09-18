package me.rainma22.dillydally.conf;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

import com.sun.net.httpserver.HttpHandler;

import me.rainma22.dillydally.abstracts.Bean;

/**
 *
 */
public class ConfBean extends Bean{
    public static final String SELF_SIGN = "self-sign";
    private int httpPort = 80;
    private boolean doHttps = true;
    private int httpsPort = 443;
    private String serverUrl = SELF_SIGN;
    // private FileHandlerConfBean fileHandlerConf = new FileHandlerConfBean();
    private Map<String, Object> layoutScheme = HandlerLayoutLoader.DEFAULT_LAYOUT;

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
    public Map<String, HttpHandler> getHandlerLayout(){
        return new HandlerLayoutLoader().fromJson(new JSONObject(layoutScheme));
    }

}
