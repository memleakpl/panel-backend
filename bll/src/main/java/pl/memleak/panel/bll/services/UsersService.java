package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.KrbException;
import pl.memleak.panel.bll.dto.User;

import java.util.List;

/**
 * Created by maxmati on 11/30/16
 */
public class UsersService implements IUsersService {
    private ILdapDao ldapDao;
    private IKrbDao krbDao;

    public UsersService(ILdapDao ldapDao, IKrbDao krbDao) {
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
    }

    @Override
    public List<User> getAllUsers() {
        return this.ldapDao.getAllUsers();
    }

    @Override
    public User getUser(String username) {
        return this.ldapDao.getUser(username);
    }

    @Override
    public void createUser(User user) {
        try {
            ldapDao.createUser(user, krbDao.getRealm());
            krbDao.createPrincipal(user.getUsername(), generatePassword());
        } catch (KrbException e) {
            throw new RuntimeException(e);
            // TODO perform rollback
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            ldapDao.deleteUser(username);
            krbDao.deletePrincipal(username);
        } catch (KrbException e) {
            throw new RuntimeException(e);
            // TODO perform rollback
        }
    }

    @Override
    public void changePassword(String username, String password) {
       try {
           krbDao.changePassword(username, password);
       } catch (KrbException e) {
           throw new RuntimeException(e);
           // TODO perform rollback
       }
    }

    private String generatePassword() {
        // TODO implement password generation
        return "password"; // https://www.xkcd.com/221/
    }
}
