package pl.memleak.panel.bootstrap.infrastructure;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by maxmati on 12/18/16
 */
@Configuration
public class QuartzConfiguration {

    private ApplicationContext context;

    @Autowired
    public QuartzConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.setJobFactory(new BeanJobFactory(context.getAutowireCapableBeanFactory()));
        scheduler.start();
        return scheduler;
    }
}
