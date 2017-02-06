package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.User;

import java.util.List;

/**
 * Created by maxmati on 11/30/16
 */
public interface IUsersService {
    List<User> getAllUsers(String authorizationUser);
    User getUser(String username, String authorizationUser);

    void createUser(User user, String authorizationUser);

    void deleteUser(String username, String authorizationUser);

    boolean authenticate(String username, String password);

    void changePassword(String username, String password);

    void editUser(User user, String authorizationUser);

    List<String> getUserGroups(String username, String authorizationUser);

    boolean isAdmin(String name);

    void generatePasswordReset(String username, String mail);

    void activatePasswordReset(String token);
}
