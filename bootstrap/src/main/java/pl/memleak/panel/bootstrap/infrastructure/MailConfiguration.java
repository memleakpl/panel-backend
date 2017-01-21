package pl.memleak.panel.bootstrap.infrastructure;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import pl.meleak.panel.infrastructure.mail.IMailDao;
import pl.meleak.panel.infrastructure.mail.MailConfig;
import pl.meleak.panel.infrastructure.mail.MailExecutor;
import pl.meleak.panel.infrastructure.mail.MailService;
import pl.memleak.panel.bll.mail.NewPasswordMailBuilder;
import pl.memleak.panel.bll.mail.PasswordRequestMailBuilder;
import pl.memleak.panel.bll.mail.UserCreatedMailBuilder;
import pl.memleak.panel.bootstrap.dal.HibernateConfiguration;

/**
 * Created by maxmati on 12/18/16
 */
@Import({QuartzConfiguration.class, HibernateConfiguration.class})
@PropertySource("classpath:mail.properties")
@PropertySource(value = "file:/etc/memleak/mail.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${MAIL_CONFIG}", ignoreResourceNotFound = true)
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
    public MailService mailService(@Qualifier(value = "scheduler") Scheduler scheduler,
                                   @Qualifier(value = "mailDao") IMailDao mailDao){
        return new MailService(scheduler, mailDao);
    }

    @Bean
    public UserCreatedMailBuilder userCreatedMailBuilder() {
        return new UserCreatedMailBuilder(
                env.getProperty("envelope.sender"),
                env.getProperty("envelope.createUserSubject"));
    }

    @Bean
    public PasswordRequestMailBuilder passwordRequestMailBuilder() {
        return new PasswordRequestMailBuilder(
                env.getProperty("envelope.sender"),
                env.getProperty("envelope.passwordResetRequestSubject"),
                env.getProperty("envelope.passwordResetAddress"));
    }

    @Bean
    public NewPasswordMailBuilder newPasswordMailBuilder() {
        return new NewPasswordMailBuilder(
                env.getProperty("envelope.sender"),
                env.getProperty("envelope.createUserSubject"));
    }
}
