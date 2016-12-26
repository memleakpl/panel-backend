package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.bll.services.IGroupsService;
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

    @RequestMapping(method = RequestMethod.DELETE, value = "/{groupname}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable String groupname){
        try {
            groupsService.deleteGroup(groupname);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e);
        }
    }
}
