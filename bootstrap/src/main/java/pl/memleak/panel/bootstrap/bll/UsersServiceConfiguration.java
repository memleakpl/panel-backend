package pl.memleak.panel.bootstrap.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import pl.meleak.panel.infrastructure.mail.MailService;
import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.IResetTokenDao;
import pl.memleak.panel.bll.mail.NewPasswordMailBuilder;
import pl.memleak.panel.bll.mail.PasswordRequestMailBuilder;
import pl.memleak.panel.bll.mail.UserCreatedMailBuilder;
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
@PropertySource("classpath:application.properties")
@PropertySource(value = "file:/etc/memleak/application.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${APP_CONFIG}", ignoreResourceNotFound = true)
public class UsersServiceConfiguration {
    private static final String ADMIN_GROUP_KEY = "adminGroup";
    private final UserCreatedMailBuilder userCreatedMailBuilder;
    private final PasswordRequestMailBuilder passwordRequestMailBuilder;
    private final NewPasswordMailBuilder newPasswordMailBuilder;
    private final ILdapDao ldapDao;
    private final IKrbDao krbDao;
    private final IResetTokenDao resetTokenDao;
    private final MailService mailService;

    public UsersServiceConfiguration(
            @Qualifier("passwordRequestMailBuilder") PasswordRequestMailBuilder passwordRequestMailBuilder,
            @Qualifier("newPasswordMailBuilder") NewPasswordMailBuilder newPasswordMailBuilder,
            @Qualifier("ldapDao") ILdapDao ldapDao,
            @Qualifier("krbDao") IKrbDao krbDao,
            @Qualifier("mailService") MailService mailService,
            @Qualifier("userCreatedMailBuilder") UserCreatedMailBuilder userCreatedMailBuilder,
            @Qualifier("resetTokenDao") IResetTokenDao resetTokenDao) {
        this.passwordRequestMailBuilder = passwordRequestMailBuilder;
        this.newPasswordMailBuilder = newPasswordMailBuilder;
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
        this.mailService = mailService;
        this.userCreatedMailBuilder = userCreatedMailBuilder;
        this.resetTokenDao = resetTokenDao;
    }

    @Bean(name = "usersService")
    public IUsersService usersService(@Autowired Environment env) {
        return new UsersService(
                ldapDao, krbDao, resetTokenDao, mailService,
                userCreatedMailBuilder, passwordRequestMailBuilder,
                newPasswordMailBuilder, env.getProperty(ADMIN_GROUP_KEY));
    }
}
