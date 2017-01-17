package pl.memleak.panel.presentation.dto;

/**
 * Created by wigdis on 14.01.17.
 */
public class PasswordResetConfirmRequest {
    private String username;
    private String token;

    public PasswordResetConfirmRequest(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
