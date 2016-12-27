package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.Group;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public interface IGroupsService {
    List<Group> getAllGroups();

    void createGroup(Group group);
    void deleteGroup(String groupname);
}
