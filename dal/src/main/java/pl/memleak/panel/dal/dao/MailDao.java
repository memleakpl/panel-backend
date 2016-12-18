package pl.memleak.panel.dal.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pl.meleak.panel.infrastructure.mail.IMailDao;
import pl.meleak.panel.infrastructure.mail.MailJobDto;
import pl.memleak.panel.dal.dto.DbMail;
import pl.memleak.panel.dal.mapper.MailMapper;

/**
 * Created by maxmati on 12/18/16
 */
public class MailDao implements IMailDao {
    private SessionFactory sessionFactory;

    public MailDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MailJobDto getById(int id) {
        final Session currentSession = sessionFactory.getCurrentSession();

        Transaction tx = currentSession.beginTransaction();
        DbMail dbMail = currentSession.byId(DbMail.class).load(id);
        tx.commit();

        return MailMapper.toMailJobDto(dbMail);
    }

    @Override
    public int save(MailJobDto mailJobDto) {
        final Session currentSession = sessionFactory.getCurrentSession();
        DbMail dbMail = MailMapper.toDbMail(mailJobDto);

        Transaction tx = currentSession.beginTransaction();
        int id = (int) currentSession.save(dbMail);
        tx.commit();

        return id;
    }

    @Override
    public void update(MailJobDto mailJobDto) {
        final Session currentSession = sessionFactory.getCurrentSession();
        DbMail dbMail = MailMapper.toDbMail(mailJobDto);

        Transaction tx = currentSession.beginTransaction();
        currentSession.update(dbMail);
        tx.commit();
    }
}
