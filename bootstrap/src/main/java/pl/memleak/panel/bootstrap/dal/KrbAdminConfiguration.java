package pl.memleak.panel.bootstrap.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.dal.configuration.KrbConfig;
import pl.memleak.panel.dal.dao.KrbDao;

/**
 * Created by maxmati on 11/30/16
 */

@Configuration
@PropertySource("classpath:krbadmin.properties")
@PropertySource(value = "file:/etc/memleak/krbadmin.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${KRBADMIN_CONFIG}", ignoreResourceNotFound = true)
public class KrbAdminConfiguration {
    private final Environment environment;

    @Autowired
    public KrbAdminConfiguration(Environment env){
        Assert.notNull(env,"Environment must not be null!");
        this.environment = env;
    }

    @Bean(name = "krbDao")
    public IKrbDao krbDao(){
        return new KrbDao(new KrbConfig(
                environment.getProperty("krbadmin.jniAbsolutePath"),
                environment.getProperty("krbadmin.principal"),
                environment.getProperty("krbadmin.keytab")));
    }
}
