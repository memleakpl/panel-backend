package pl.memleak.panel.presentation.dto;

/**
 * Created by wigdis on 14.01.17.
 */
public class PasswordResetRequest {
    private String username;
    private String mail;

    public PasswordResetRequest(String username, String mail) {
        this.username = username;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
