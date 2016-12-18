package pl.meleak.panel.infrastructure.mail;


import org.quartz.*;
import pl.memleak.panel.bll.dto.Mail;
import pl.memleak.panel.bll.services.IMailService;

/**
 * Created by maxmati on 12/17/16
 */
public class MailService implements IMailService {
    private Scheduler scheduler;
    private IMailDao mailDao;

    public MailService(Scheduler scheduler, IMailDao mailDao) {
        this.scheduler = scheduler;
        this.mailDao = mailDao;
    }

    public void sendMail(Mail mail)  {
        JobDetail job = JobBuilder.newJob(MailJob.class).build();

        int mailId = mailDao.save(MailMapper.toMailJobDto(mail));

        JobDataMap dataMap = new JobDataMap();
        dataMap.put(MailJob.MAIL_ID_KEY, mailId);

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .usingJobData(dataMap)
                .startNow()
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
