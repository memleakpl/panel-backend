package pl.memleak.panel.bootstrap.bll;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.bll.services.UsersService;
import pl.memleak.panel.bootstrap.dal.KrbConfiguration;
import pl.memleak.panel.bootstrap.dal.LdaptiveConfiguration;

/**
 * Created by maxmati on 11/30/16
 */
@Import({LdaptiveConfiguration.class, KrbConfiguration.class})
@Configuration
public class UsersServiceConfiguration {
    private ILdapDao ldapDao;
    private IKrbDao krbDao;

    public UsersServiceConfiguration(
            @Qualifier("ldapDao") ILdapDao ldapDao,
            @Qualifier("krbDao") IKrbDao krbDao) {
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
    }

    @Bean(name = "usersService")
    public IUsersService usersService(){
        return new UsersService(ldapDao, krbDao);
    }
}
