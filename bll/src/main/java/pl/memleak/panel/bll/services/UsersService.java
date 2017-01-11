package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.KrbException;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.mail.UserCreatedMailBuilder;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by maxmati on 11/30/16
 */
public class UsersService implements IUsersService {
    private ILdapDao ldapDao;
    private IKrbDao krbDao;
    private IMailService mailService;
    private UserCreatedMailBuilder userCreatedMailBuilder;
    private Random random = new Random();

    public UsersService(ILdapDao ldapDao, IKrbDao krbDao, IMailService mailService, UserCreatedMailBuilder
            userCreatedMailBuilder) {
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
        this.mailService = mailService;
        this.userCreatedMailBuilder = userCreatedMailBuilder;
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
            String password = generatePassword();

            ldapDao.createUser(user, krbDao.getRealm());
            krbDao.createPrincipal(user.getUsername(), password);

            userCreatedMailBuilder.setUser(user);
            userCreatedMailBuilder.setPassword(password);

            mailService.sendMail(userCreatedMailBuilder.build());
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
    public boolean authenticate(String username, String password) {
        return ldapDao.authenticate(username, password);
    }

    @Override
    public void changePassword(String username, String password) {
        try {
            krbDao.changePassword(username, password);
        } catch (KrbException e) {
            throw new RuntimeException(e);
        }
    }

    private String generatePassword() {
        @SuppressWarnings("SpellCheckingInspection")
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public void editUser(User user) {
            ldapDao.editUser(user);
    }

    @Override
    public List<String> getUserGroups(String username) {
        return ldapDao.getUserGroups(username).stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }
}
