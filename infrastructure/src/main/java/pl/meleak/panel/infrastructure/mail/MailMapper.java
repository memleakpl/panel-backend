package pl.meleak.panel.infrastructure.mail;


import pl.memleak.panel.bll.dto.Mail;

/**
 * Created by maxmati on 12/18/16
 */
public class MailMapper {
    public static MailJobDto toMailJobDto(Mail mail){
        return new MailJobDto(
                null,
                false,
                mail.getFrom(),
                mail.getTo(),
                mail.getSubject(),
                mail.getBody()
        );
    }
}
