package pl.memleak.panel.bootstrap.dal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.dal.dao.KrbDao;

/**
 * Created by maxmati on 11/30/16
 */

@Configuration
public class KrbConfiguration {

    @Bean(name = "krbDao")
    public IKrbDao krbDao(){
        return new KrbDao();
    }
}
