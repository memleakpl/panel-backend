package pl.memleak.panel.bootstrap.infrastructure;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Created by maxmati on 12/18/16
 */
public class BeanJobFactory implements JobFactory{
    private AutowireCapableBeanFactory beanFactory;

    public BeanJobFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetails = bundle.getJobDetail();
        Class<? extends Job> jobClass = jobDetails.getJobClass();
        return beanFactory.createBean(jobClass);
    }
}
