package pl.memleak.panel.bootstrap.bll;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.services.GroupsService;
import pl.memleak.panel.bll.services.IGroupsService;
import pl.memleak.panel.bootstrap.dal.LdaptiveConfiguration;

/**
 * Created by maxmati on 12/22/16
 */
@Import(LdaptiveConfiguration.class)
@Configuration
public class GroupsServiceConfiguration {
    private ILdapDao ldapDao;

    public GroupsServiceConfiguration(@Qualifier("ldapDao") ILdapDao ldapDao) {
        this.ldapDao = ldapDao;
    }

    @Bean(name = "groupsService")
    public IGroupsService usersService(){
        return new GroupsService(ldapDao);
    }
}
