package pl.memleak.panel.bll.mail;

import pl.memleak.panel.bll.dto.Mail;
import pl.memleak.panel.bll.dto.User;

/**
 * Created by wigdis on 22.01.17.
 */
public class NewPasswordMailBuilder implements MailBuilder{
    private final String sender;
    private final String subject;
    private User user;
    private String password;

    public NewPasswordMailBuilder(String sender, String subject) {
        this.sender = sender;
        this.subject = subject;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Mail build() {
        if(user == null) throw new RuntimeException("User not provided");
        if(password == null) throw new RuntimeException("New password not provided");

        return new Mail(sender, user.getEmail(), subject, formatMessage(user.getFirstName(), password));
    }

    private String formatMessage(String name, String password) {
        return String.format("Hello %s, your new password is %s", name, password);
    }
}
