package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;
import pl.memleak.panel.bll.services.IGroupsService;
import pl.memleak.panel.presentation.exceptions.ForbiddenException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;

import java.util.List;

import static pl.memleak.panel.presentation.controllers.Utils.getCurrentUser;

/**
 * Created by maxmati on 12/22/16
 */
@RestController
@RequestMapping("/api/group")
public class GroupsController {

    private final IGroupsService groupsService;

    public GroupsController(@Qualifier("groupsService") IGroupsService groupsService) {
        this.groupsService = groupsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Group> getGroups(){
        try {
            return groupsService.getAllGroups(getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody Group group) {
        try {
            groupsService.createGroup(group, getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{groupname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable String groupname){
        try {
            groupsService.deleteGroup(groupname, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{groupname}/add/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToGroup(@PathVariable String groupname, @PathVariable String username){
        try {
            groupsService.addToGroup(groupname, username, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{groupname}/remove/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromGroup(@PathVariable String groupname, @PathVariable String username){
        try {
            groupsService.removeFromGroup(groupname, username, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }
}
