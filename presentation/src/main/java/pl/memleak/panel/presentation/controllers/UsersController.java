package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.ChangePasswordRequest;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;
import pl.memleak.panel.bll.services.IUsersService;
import pl.memleak.panel.presentation.dto.IsAdminResponse;
import pl.memleak.panel.presentation.dto.PasswordResetConfirmRequest;
import pl.memleak.panel.presentation.dto.PasswordResetRequest;
import pl.memleak.panel.presentation.dto.UserRequest;
import pl.memleak.panel.presentation.exceptions.BadRequestException;
import pl.memleak.panel.presentation.exceptions.ForbiddenException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;
import pl.memleak.panel.presentation.exceptions.UnauthorizedException;
import pl.memleak.panel.presentation.mappers.UserMapper;

import javax.validation.Valid;
import java.util.List;

import static pl.memleak.panel.presentation.controllers.Utils.getCurrentUser;

/**
 * Created by maxmati on 11/26/16
 */
@RestController
@RequestMapping("/api/user")
public class UsersController {
    private final IUsersService usersService;

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
    public void createUser(@RequestBody @Valid UserRequest user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        try {
            usersService.createUser(UserMapper.toUser(user), getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

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

    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editUser(
            @RequestBody @Valid UserRequest user,
            @PathVariable String username,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        if (!user.getUsername().equals(username)) {
            throw new BadRequestException("Username doesn't match");
        }

        try {
            usersService.editUser(UserMapper.toUser(user), getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/isAdmin")
    public IsAdminResponse isAdmin(){
        IsAdminResponse response = new IsAdminResponse();
        response.setAdmin(usersService.isAdmin(getCurrentUser()));
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value =" /{username}")
    public void generatePasswordReset(@RequestBody PasswordResetRequest request, @PathVariable String username){
        if (!request.getUsername().equals(username)) {
            throw new BadRequestException("Username doesn't match");
        }

        usersService.generatePasswordReset(request.getUsername(), request.getMail());
    }

    @RequestMapping(method = RequestMethod.POST, value ="/{username}")
    public void activatePasswordReset(@RequestBody PasswordResetConfirmRequest request, @PathVariable String username){
        if (!request.getUsername().equals(username)) {
            throw new BadRequestException("Username doesn't match");
        }

        usersService.activatePasswordReset(request.getUsername(), request.getToken());
    }
}
