package pl.memleak.panel.bootstrap.dal;

import org.ldaptive.BindConnectionInitializer;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.Credential;
import org.ldaptive.DefaultConnectionFactory;
import org.ldaptive.sasl.Mechanism;
import org.ldaptive.sasl.SaslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.dal.configuration.LdapConfig;
import pl.memleak.panel.dal.dao.LdapDao;

/**
 * Created by Kamil on 19.11.2016.
 */
@Configuration
@PropertySource("classpath:ldapconfig.properties")
@PropertySource(value = "file:/etc/memleak/ldapconfig.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${LDAP_CONFIG}", ignoreResourceNotFound = true)
public class LdaptiveConfiguration {
    private Environment environment;

    @Autowired
    public LdaptiveConfiguration(Environment env){
        Assert.notNull(env,"Environment must not be null!");
        this.environment = env;
    }

    @Bean(name = "connectionFactory")
    public DefaultConnectionFactory connectionFactory(){
        return new DefaultConnectionFactory(connectionConfig());
    }

    private ConnectionConfig connectionConfig(){
        final SaslConfig saslConfig = new SaslConfig();
        saslConfig.setMechanism(Mechanism.GSSAPI);

        final BindConnectionInitializer connectionInitializer = new BindConnectionInitializer();
        connectionInitializer.setBindSaslConfig(saslConfig);

        ConnectionConfig connConfig = new ConnectionConfig(environment.getProperty("ldap.url"));
        connConfig.setConnectionInitializer(connectionInitializer);
        return connConfig;
    }

    @Bean(name = "ldapDao")
    public ILdapDao ldapDao(){
        return new LdapDao(connectionFactory(), ldapConfig());
    }

    @Bean(name = "ldapConfig")
    public LdapConfig ldapConfig(){
        return new LdapConfig(
            environment.getProperty("ldap.query.getUser"),
            environment.getProperty("ldap.query.getGroup"),
            environment.getProperty("ldap.query.getAllUsers"),
            environment.getProperty("ldap.baseDn.user.default"),
            environment.getProperty("ldap.baseDn.group.default")
        );
    }
}