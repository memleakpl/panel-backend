package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.ChangePasswordRequest;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.presentation.dto.UserRequest;
import pl.memleak.panel.presentation.exceptions.BadRequestException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;
import pl.memleak.panel.presentation.exceptions.UnauthorizedException;
import pl.memleak.panel.presentation.mappers.UserMapper;

import javax.validation.Valid;
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
    public void createUser(@RequestBody @Valid UserRequest user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        usersService.createUser(UserMapper.toUser(user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public User getUser(@PathVariable String username) {
        try {
            return usersService.getUser(username);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/groups")
    public List<String> getUserGroups(@PathVariable String username){
        try {
            return usersService.getUserGroups(username);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        try {
            usersService.deleteUser(username);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editUser(@RequestBody @Valid User user, @PathVariable String username, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        if (!user.getUsername().equals(username)) {
            throw new BadRequestException("Username doesn't match");
        }

        usersService.editUser(user);
    }
}