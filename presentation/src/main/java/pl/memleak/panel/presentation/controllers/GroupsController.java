package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;
import pl.memleak.panel.bll.services.IGroupsService;
import pl.memleak.panel.presentation.dto.GroupRequest;
import pl.memleak.panel.presentation.exceptions.BadRequestException;
import pl.memleak.panel.presentation.exceptions.ForbiddenException;
import pl.memleak.panel.presentation.exceptions.NotFoundException;
import pl.memleak.panel.presentation.mappers.GroupMapper;

import javax.validation.Valid;
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
    public void createUser(@RequestBody @Valid GroupRequest group, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        try {
            groupsService.createGroup(GroupMapper.toGroup(group), getCurrentUser());
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

    @RequestMapping(method = RequestMethod.GET, value = "/{groupname}")
    public Group getGroup(@PathVariable String groupname) {
        try {
            return groupsService.getGroup(groupname, getCurrentUser());
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{groupname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editGroup(@PathVariable String groupname,
                          @RequestBody @Valid GroupRequest group,
                          BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        if ( !groupname.equals(group.getName())){
            throw new BadRequestException("Groupname doesn't match");
        }
        try {
            groupsService.editGroup(GroupMapper.toGroup(group), getCurrentUser());
        } catch (OperationNotPermittedException e) {
            throw new ForbiddenException(e);
        }
    }
}
