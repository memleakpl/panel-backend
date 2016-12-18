package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.Mail;

/**
 * Created by maxmati on 12/17/16
 */
public interface IMailService {
    void sendMail(Mail mail);
}
