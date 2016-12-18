package pl.memleak.panel.presentation.configuration;

/**
 * Created by wigdis on 12.12.16.
 */
public class LoginConfig {
    private String userSearchBase;
    private String userSearchFilter;
    private String url;


    public LoginConfig(String userSearchBase, String userSearchFilter, String url) {
        this.userSearchBase = userSearchBase;
        this.userSearchFilter = userSearchFilter;
        this.url = url;
    }


    public String getUserSearchBase() {
        return userSearchBase;
    }

    public String getUserSearchFilter() {
        return userSearchFilter;
    }

    public String getUrl() {
        return url;
    }
}
