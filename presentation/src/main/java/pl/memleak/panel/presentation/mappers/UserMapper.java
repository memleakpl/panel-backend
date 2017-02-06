package pl.memleak.panel.presentation.mappers;

import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.presentation.dto.UserRequest;

/**
 * Created by maxmati on 1/12/17
 */
public class UserMapper {
    public static User toUser(UserRequest userRequest) {
        return new User(userRequest.getUsername(),
                userRequest.getFirstName(),
                userRequest.getEmail(),
                userRequest.getLastName());
    }
}
