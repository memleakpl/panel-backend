package pl.meleak.panel.infrastructure.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by maxmati on 12/18/16
 */
public class MailExecutor {
    private Session session;

    public MailExecutor(MailConfig config) {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", config.getStartTls());
        props.put("mail.smtp.auth", config.getAuth());
        props.put("mail.smtp.host", config.getHostname());
        props.put("mail.smtp.port", config.getPort());

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
    }

    public void sendMail(MailJobDto mail){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFrom()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
            message.setSubject(mail.getSubject());
            message.setText(mail.getBody());

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
