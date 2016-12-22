package pl.memleak.panel.presentation.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.services.IGroupsService;

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
    public List<Group> getUsers(){
        return groupsService.getAllGroups();
    }
}
