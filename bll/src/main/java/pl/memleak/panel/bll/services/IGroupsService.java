package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dto.Group;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public interface IGroupsService {
    List<Group> getAllGroups();

    void deleteGroup(String groupname);
}
