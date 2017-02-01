package pl.memleak.panel.bootstrap.dal;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.meleak.panel.infrastructure.mail.IMailDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.IResetTokenDao;
import pl.memleak.panel.dal.dao.LdapDao;
import pl.memleak.panel.dal.dao.MailDao;
import pl.memleak.panel.dal.dao.ResetTokenDao;
import pl.memleak.panel.dal.dto.DbMail;
import pl.memleak.panel.dal.dto.DbResetToken;

/**
 * Created by maxmati on 12/18/16
 */

@Import({LdaptiveConfiguration.class})
@Configuration
public class HibernateConfiguration {

    @Bean
    public IMailDao mailDao(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory){
        return new MailDao(sessionFactory);
    }

    @Bean
    public IResetTokenDao resetTokenDao(
            @Qualifier(value = "sessionFactory") SessionFactory sessionFactory,
            @Qualifier(value = "ldapDao") ILdapDao ldapDao){
        return new ResetTokenDao(sessionFactory, ldapDao);
    }

    @Bean
    public SessionFactory sessionFactory()
    {
        ServiceRegistry standardRegistry =
                new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();

        Metadata metadata = new MetadataSources( standardRegistry )
                .addAnnotatedClass(DbMail.class)
                .addAnnotatedClass(DbResetToken.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

        return sessionFactoryBuilder.build();
    }
}
