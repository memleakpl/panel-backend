package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.ChangePasswordRequest;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.presentation.exceptions.UnauthorizedException;

import java.util.List;

/**
 * Created by maxmati on 11/26/16
 */
@RestController
@RequestMapping("/api/user")
public class UsersController {
    private IUsersService usersService;

    public UsersController(@Qualifier(value = "usersService") IUsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<User> getUsers() {
        return usersService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody User user) {
        usersService.createUser(user);
    } //TODO: model validation

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public User getUser(@PathVariable String username) {
        return usersService.getUser(username);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        usersService.deleteUser(username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String username;

        if (principal == null)
            throw new RuntimeException("Missing authentication principal");

        username = principal.getName();

        if (!usersService.authenticate(username, changePasswordRequest.getOldPassword()))
            throw new UnauthorizedException("Invalid credentials");

        usersService.changePassword(username, changePasswordRequest.getNewPassword());
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{username}")
    public void editUser(@RequestBody User user, @PathVariable String username){
            usersService.editUser(user, username);
    }
}