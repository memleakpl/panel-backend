package pl.memleak.panel.bootstrap.infrastructure;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import pl.meleak.panel.infrastructure.mail.IMailDao;
import pl.meleak.panel.infrastructure.mail.MailConfig;
import pl.meleak.panel.infrastructure.mail.MailExecutor;
import pl.meleak.panel.infrastructure.mail.MailService;
import pl.memleak.panel.bootstrap.dal.HibernateConfiguration;

/**
 * Created by maxmati on 12/18/16
 */
@Import({QuartzConfiguration.class, HibernateConfiguration.class})
@PropertySource("classpath:smtp.properties")
@PropertySource(value = "file:/etc/memleak/smtp.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${LDAP_CONFIG}", ignoreResourceNotFound = true)
@Configuration
public class MailConfiguration {
    private Environment env;

    public MailConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public MailExecutor mailExecutor(){
        MailConfig configuration = new MailConfig(
                env.getProperty("smtp.username"),
                env.getProperty("smtp.password"),
                env.getProperty("smtp.hostname"),
                env.getProperty("smtp.port"),
                env.getProperty("smtp.auth"),
                env.getProperty("smtp.starttls"));
        return new MailExecutor(configuration);
    }

    @Bean
    public MailService mailService(Scheduler scheduler, IMailDao mailDao){
        return new MailService(scheduler, mailDao);
    }
}
