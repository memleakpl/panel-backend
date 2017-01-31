package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dao.IResetTokenDao;
import pl.memleak.panel.bll.dao.KrbException;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;
import pl.memleak.panel.bll.mail.PasswordRequestMailBuilder;
import pl.memleak.panel.bll.mail.UserCreatedMailBuilder;
import pl.memleak.panel.bll.utils.RandomSequenceGenerator;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by maxmati on 11/30/16
 */
public class UsersService implements IUsersService {
    private final ILdapDao ldapDao;
    private final IKrbDao krbDao;
    private final IResetTokenDao resetTokenDao;
    private final IMailService mailService;
    private final UserCreatedMailBuilder userCreatedMailBuilder;
    private final PasswordRequestMailBuilder passwordRequestMailBuilder;
    private final String adminGroupName;
    private static final String PASSWORD_PATTERN =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()";
    private static final String TOKEN_PATTERN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public UsersService(ILdapDao ldapDao, IKrbDao krbDao, IResetTokenDao resetTokenDao, IMailService mailService,
                        UserCreatedMailBuilder userCreatedMailBuilder,
                        PasswordRequestMailBuilder passwordRequestMailBuilder, String adminGroupName) {
        this.ldapDao = ldapDao;
        this.krbDao = krbDao;
        this.resetTokenDao = resetTokenDao;
        this.mailService = mailService;
        this.userCreatedMailBuilder = userCreatedMailBuilder;
        this.passwordRequestMailBuilder = passwordRequestMailBuilder;
        this.adminGroupName = adminGroupName;
    }

    @Override
    public List<User> getAllUsers(String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        return this.ldapDao.getAllUsers();
    }

    @Override
    public User getUser(String username, String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        return this.ldapDao.getUser(username);
    }

    @Override
    public void createUser(User user, String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

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
    public void deleteUser(String username, String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

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
        return RandomSequenceGenerator.generate(20, PASSWORD_PATTERN);
    }

    private String generateToken() {
        return RandomSequenceGenerator.generate(10, TOKEN_PATTERN);
    }

    @Override
    public void editUser(User user, String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        ldapDao.editUser(user);
    }

    @Override
    public List<String> getUserGroups(String username, String authorizationUser) {
        if(!isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        return ldapDao.getUserGroups(username).stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdmin(String name) {
        List<Group> groups = ldapDao.getUserGroups(name);
        return groups.stream().anyMatch(group -> group.getName().equals(adminGroupName));
    }

    @Override
    public void generatePasswordReset(String username, String email) {
        User user = ldapDao.getUser(username);
        if(!user.getEmail().equals(email)) throw new OperationNotPermittedException("Email mismatch");

        final String token = generateToken();

        resetTokenDao.addToken(user.getUsername(), token, LocalDateTime.now().plusHours(1));

        passwordRequestMailBuilder.setToken(token);
        passwordRequestMailBuilder.setUser(user);
        mailService.sendMail(passwordRequestMailBuilder.build());
    }

    @Override
    public void activatePasswordReset(String username, String token) {
        User user = ldapDao.getUser(username);
        final String tokenFromDb = resetTokenDao.getToken(user.getUsername(), LocalDateTime.now());

        if(!token.equals(tokenFromDb)) throw new OperationNotPermittedException("Wrong token");

        try {
            krbDao.changePassword(user.getUsername(),  generatePassword());
        } catch (KrbException e) {
            throw new RuntimeException(e);
        }
    }
}
