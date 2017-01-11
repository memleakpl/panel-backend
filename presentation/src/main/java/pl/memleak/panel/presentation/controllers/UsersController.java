package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.ChangePasswordRequest;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.presentation.dto.IsAdminResponse;
import pl.memleak.panel.presentation.exceptions.BadRequestException;
import pl.memleak.panel.presentation.exceptions.ForbiddenException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;
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
        try {
            return usersService.getAllUsers(getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody User user) {
        try {
            usersService.createUser(user, getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    } //TODO: model validation

    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public User getUser(@PathVariable String username) {
        try {
            return usersService.getUser(username, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/groups")
    public List<String> getUserGroups(@PathVariable String username){
        try {
            return usersService.getUserGroups(username, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        try {
            usersService.deleteUser(username, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        String username = getCurrentUser();

        if (!usersService.authenticate(username, changePasswordRequest.getOldPassword()))
            throw new UnauthorizedException("Invalid credentials");

        usersService.changePassword(username, changePasswordRequest.getNewPassword());
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editUser(@RequestBody User user, @PathVariable String username) {
        if (!user.getUsername().equals(username)) {
            throw new BadRequestException("Username doesn't match");
        }

        try {
            usersService.editUser(user, getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/isAdmin")
    public IsAdminResponse isAdmin(){
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        if (principal == null)
            throw new RuntimeException("Missing authentication principal");

        IsAdminResponse response = new IsAdminResponse();
        response.setAdmin(usersService.isAdmin(principal.getName()));
        return response;
    }

    private String getCurrentUser() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        if (principal == null)
            throw new RuntimeException("Missing authentication principal");

        return principal.getName();
    }
}