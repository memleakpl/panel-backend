package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.User;

import java.util.List;

/**
 * Created by maxmati on 11/30/16
 */
public interface IUsersService {
    List<User> getAllUsers();

    void createUser(User user);

    void deleteUser(String username);
}
