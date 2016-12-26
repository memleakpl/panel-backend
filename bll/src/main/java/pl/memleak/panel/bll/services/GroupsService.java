package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dto.Group;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public class GroupsService implements IGroupsService {

    private ILdapDao ldapDao;

    public GroupsService(ILdapDao ldapDao) {
        this.ldapDao = ldapDao;
    }

    @Override
    public List<Group> getAllGroups() {
        return ldapDao.getAllGroups();
    }

    @Override
    public void deleteGroup(String groupname) {
        ldapDao.deleteGroup(groupname);
    }
}
