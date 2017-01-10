package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.services.IGroupsService;
import pl.memleak.panel.presentation.exceptions.BadRequestException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
@RestController
@RequestMapping("/api/group")
public class GroupsController {

    private IGroupsService groupsService;

    public GroupsController(@Qualifier("groupsService") IGroupsService groupsService) {
        this.groupsService = groupsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Group> getGroups(){
        return groupsService.getAllGroups();
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody Group group) {
        groupsService.createGroup(group);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{groupname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable String groupname){
        try {
            groupsService.deleteGroup(groupname);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{groupname}/add/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToGroup(@PathVariable String groupname, @PathVariable String username){
        try {
            groupsService.addToGroup(groupname, username);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{groupname}/remove/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromGroup(@PathVariable String groupname, @PathVariable String username){
        try {
            groupsService.removeFromGroup(groupname, username);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{groupname}")
    @ResponseStatus(HttpStatus.OK)
    public Group getGroup(@PathVariable String groupname) {
        try {
            return groupsService.getGroup(groupname);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/{groupname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editGroup(@PathVariable String groupname, Group group){
        if ( !groupname.equals(group.getName())){
            throw new BadRequestException("Groupname doesn't match");
        }
        groupsService.editGroup(group);
    }
}
