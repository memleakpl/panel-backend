package pl.memleak.panel.dal.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pl.meleak.panel.infrastructure.mail.MailJobDto;
import pl.memleak.panel.dal.dto.DbMail;
import pl.memleak.panel.dal.mapper.MailMapper;

/**
 * Created by wigdis on 29.01.17.
 */
public class ResetTokenDao {
    private SessionFactory sessionFactory;

    public ResetTokenDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    @Override
//    public MailJobDto getById(int id) {
//        final Session currentSession = sessionFactory.getCurrentSession();
//
//        Transaction tx = currentSession.beginTransaction();
//        DbMail dbMail = currentSession.byId(DbMail.class).load(id);
//        tx.commit();
//
//        return MailMapper.toMailJobDto(dbMail);
//    }
//
//    @Override
//    public int save(MailJobDto mailJobDto) {
//        final Session currentSession = sessionFactory.getCurrentSession();
//        DbMail dbMail = MailMapper.toDbMail(mailJobDto);
//
//        Transaction tx = currentSession.beginTransaction();
//        int id = (int) currentSession.save(dbMail);
//        tx.commit();
//
//        return id;
//    }
//
//    @Override
//    public void update(MailJobDto mailJobDto) {
//        final Session currentSession = sessionFactory.getCurrentSession();
//        DbMail dbMail = MailMapper.toDbMail(mailJobDto);
//
//        Transaction tx = currentSession.beginTransaction();
//        currentSession.update(dbMail);
//        tx.commit();
//    }
}
