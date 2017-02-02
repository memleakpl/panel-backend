package pl.memleak.panel.dal.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.IResetTokenDao;
import pl.memleak.panel.dal.dto.DbResetToken;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by wigdis on 29.01.17.
 */
public class ResetTokenDao implements IResetTokenDao {
    private final SessionFactory sessionFactory;
    private final ILdapDao ldapDao;

    public ResetTokenDao(SessionFactory sessionFactory, ILdapDao ldapDao) {
        this.sessionFactory = sessionFactory;
        this.ldapDao = ldapDao;
    }

    @Override
    public void addToken(String username, String token, LocalDateTime dueDate) {
        String userDn = ldapDao.getUserDn(username);
        final Session currentSession = sessionFactory.getCurrentSession();
        DbResetToken resetToken = new DbResetToken(token, userDn, dueDate);

        Transaction tx = currentSession.beginTransaction();
        currentSession.save(resetToken);
        tx.commit();
    }

    @Override
    public Optional<String> getToken(String token, LocalDateTime dueDate) {
        final Session currentSession = sessionFactory.getCurrentSession();

        Transaction tx = currentSession.beginTransaction();
        Query<DbResetToken> query = currentSession
                .createQuery("FROM DbResetToken WHERE token = :token AND expirationDate > :dueDate",
                        DbResetToken.class)
                .setParameter("token", token)
                .setParameter("dueDate", dueDate);
        final Optional<String> userDn = query.list().stream().map(DbResetToken::getUserDn).findFirst();
        tx.commit();
        return userDn.map(s -> ldapDao.getUserByDn(s).getUsername());

    }

    @Override
    public void removeToken(String token) {
        final Session currentSession = sessionFactory.getCurrentSession();

        Transaction tx = currentSession.beginTransaction();
        currentSession.createQuery("DELETE DbResetToken WHERE token = :token")
                .setParameter("token", token)
                .executeUpdate();
        tx.commit();
    }
}
