package pl.meleak.panel.infrastructure.mail;

/**
 * Created by maxmati on 12/18/16
 */
public interface IMailDao {
    MailJobDto getById(int id);
    int save(MailJobDto mail);
    void update(MailJobDto mailJobDto);
}
