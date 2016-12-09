package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
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
        String realm = krbDao.getRealm();
        ldapDao.createUser(user, realm);
        krbDao.createPrincipal(user.getUsername());
    }

    @Override
    public void deleteUser(String username) {
        ldapDao.deleteUser(username);
        krbDao.deletePrincipal(username);
    }
}
