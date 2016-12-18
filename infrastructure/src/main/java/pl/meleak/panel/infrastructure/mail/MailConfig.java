package pl.meleak.panel.infrastructure.mail;

/**
 * Created by maxmati on 12/18/16
 */
public class MailConfig {
    private String username;
    private String password;
    private String hostname;
    private String port;
    private String startTls;
    private String auth;

    public MailConfig(String username, String password, String hostname, String port, String startTls, String auth) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.startTls = startTls;
        this.auth = auth;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getStartTls() {
        return startTls;
    }

    public void setStartTls(String startTls) {
        this.startTls = startTls;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
