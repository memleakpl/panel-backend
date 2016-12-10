package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.bll.services.IUsersService;

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
    public User getUser(@PathVariable  String username){
        return usersService.getUser(username);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    public void deleteUser(@PathVariable String username){
        usersService.deleteUser(username);
    }
}