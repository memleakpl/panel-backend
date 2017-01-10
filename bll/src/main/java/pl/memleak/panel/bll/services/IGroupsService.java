package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.Group;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public interface IGroupsService {
    List<Group> getAllGroups();

    void createGroup(Group group);

    Group getGroup(String groupname);

    void deleteGroup(String groupname);

    void editGroup(Group group);

    void addToGroup(String groupname, String username);

    void removeFromGroup(String groupname, String username);
}
