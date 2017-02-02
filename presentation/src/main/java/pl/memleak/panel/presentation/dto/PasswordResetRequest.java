package pl.memleak.panel.presentation.dto;

/**
 * Created by wigdis on 14.01.17.
 */
public class PasswordResetRequest {
    private String username;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
