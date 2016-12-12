package pl.memleak.panel.bootstrap.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import pl.memleak.panel.presentation.configuration.LoginConfig;

/**
 * Created by wigdis on 12.12.16.
 */
@Configuration
@PropertySource("classpath:loginconfig.properties")
@PropertySource(value = "file:/etc/loginconfig.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${LOGIN_CONFIG}",ignoreResourceNotFound = true)
public class LoginConfiguration {
    private Environment environment;

    @Autowired
    public LoginConfiguration(Environment env){
        Assert.notNull(env,"Environment must not be null!");
        this.environment = env;
    }

    @Bean(name = "loginConfig")
    public LoginConfig loginConfig(){
        return new LoginConfig(
                environment.getProperty("ldap.search.userSearchBase"),
                environment.getProperty("ldap.search.userSearchFilter"),
                environment.getProperty("ldap.search.url")
        );
    }
}
