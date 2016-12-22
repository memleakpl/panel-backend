package pl.memleak.panel.bootstrap.bll;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.meleak.panel.infrastructure.mail.MailService;
import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.bll.services.UsersService;
import pl.memleak.panel.bootstrap.dal.KrbAdminConfiguration;
import pl.memleak.panel.bootstrap.dal.LdaptiveConfiguration;
import pl.memleak.panel.bootstrap.infrastructure.MailConfiguration;

/**
 * Created by maxmati on 11/30/16
 */
@Import({LdaptiveConfiguration.class, KrbAdminConfiguration.class, MailConfiguration.class})
@Configuration
public class UsersServiceConfiguration {
    private ILdapDao ldapDao;
    private IKrbDao krbDao;
    private MailService mailService;

    public UsersServiceConfiguration(
            @Qualifier("ldapDao") ILdapDao ldapDao,
            @Qualifier("krbDao") IKrbDao krbDao,
            @Qualifier("mailService") MailService mailService) {
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
        this.mailService = mailService;
    }

    @Bean(name = "usersService")
    public IUsersService usersService(){
        return new UsersService(ldapDao, krbDao, mailService);
    }
}
