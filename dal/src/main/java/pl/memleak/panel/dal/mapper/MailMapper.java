package pl.memleak.panel.dal.mapper;

import pl.meleak.panel.infrastructure.mail.MailJobDto;
import pl.memleak.panel.dal.dto.DbMail;

/**
 * Created by maxmati on 12/18/16
 */
public class MailMapper {
    public static DbMail toDbMail(MailJobDto mailJobDto){
        return new DbMail(
                mailJobDto.getId(),
                mailJobDto.getFrom(),
                mailJobDto.getTo(),
                mailJobDto.getSubject(),
                mailJobDto.getBody(),
                mailJobDto.getSent()
        );
    }

    public static MailJobDto toMailJobDto(DbMail dbMail) {
        return new MailJobDto(
                dbMail.getId(),
                dbMail.isSent(),
                dbMail.getSender(),
                dbMail.getRecipient(),
                dbMail.getSubject(),
                dbMail.getBody()
        );
    }
}
