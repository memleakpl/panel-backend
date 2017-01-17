package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.Group;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public interface IGroupsService {
    List<Group> getAllGroups(String authorizationUser);

    void createGroup(Group group, String authorizationUser);
    void deleteGroup(String groupname, String authorizationUser);

    Group getGroup(String groupname);

    void addToGroup(String groupname, String username, String authorizationUser);
    void editGroup(Group group);

    void removeFromGroup(String groupname, String username, String authorizationUser);
}
