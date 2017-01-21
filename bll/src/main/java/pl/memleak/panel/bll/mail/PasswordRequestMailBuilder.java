package pl.memleak.panel.bll.mail;

import pl.memleak.panel.bll.dto.Mail;
import pl.memleak.panel.bll.dto.User;

/**
 * Created by wigdis on 21.01.17.
 */
public class PasswordRequestMailBuilder implements MailBuilder{
    private final String sender;
    private final String subject;
    private User user;
    private String token;
    private final String address;

    public PasswordRequestMailBuilder(String sender, String subject, String address) {
        this.sender = sender;
        this.subject = subject;
        this.address = address;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Mail build() {
        if(user == null) throw new RuntimeException("User not provided");
        if(token == null) throw new RuntimeException("Token not provided");

        return new Mail(sender, user.getEmail(), subject, formatMessage(user.getFirstName(), address+token));
    }

    private String formatMessage(String name, String link) {
        return String.format("Hello %s, if you want to reset your password, click here:. %s" +
                "If you haven't been trying to change your password, ignore this message.", name, link);
    }
}
