package pl.memleak.panel.presentation.dto;

/**
 * Created by wigdis on 14.01.17.
 */
public class PasswordResetConfirmRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
