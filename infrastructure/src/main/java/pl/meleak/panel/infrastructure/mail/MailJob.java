package pl.meleak.panel.infrastructure.mail;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by maxmati on 12/18/16
 */
public class MailJob implements Job {
    public static final String MAIL_ID_KEY = "mailId";
    private MailExecutor mailExecutor;
    private IMailDao mailDao;

    public MailJob(MailExecutor mailExecutor, IMailDao mailDao) {
        this.mailExecutor = mailExecutor;
        this.mailDao = mailDao;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        int mailId  = data.getInt(MAIL_ID_KEY);
        MailJobDto mailJobDto = mailDao.getById(mailId);
        if(!mailJobDto.getSent()){
            mailExecutor.sendMail(mailJobDto);
            mailJobDto.setSent(true);
            mailDao.update(mailJobDto);
        }
    }
}
