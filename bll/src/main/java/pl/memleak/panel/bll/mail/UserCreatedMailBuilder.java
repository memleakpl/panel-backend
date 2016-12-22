package pl.memleak.panel.bll.mail;

import pl.memleak.panel.bll.dto.Mail;
import pl.memleak.panel.bll.dto.User;

public class UserCreatedMailBuilder implements MailBuilder {
    private final String sender;
    private final String subject;
    private User user;
    private String password;

    public UserCreatedMailBuilder(String sender, String subject) {
        this.sender = sender;
        this.subject = subject;
    }

    @Override
    public Mail build() {
        if(user == null)
            throw new RuntimeException("User not provided");
        if(password == null)
            throw new RuntimeException("Password not provided");

        return new Mail(sender, user.getEmail(), subject, formatMessage(user.getFirstName(), password));
    }

    private String formatMessage(String name, String password) {
        return String.format("Hello %s, your password is %s", name, password);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
