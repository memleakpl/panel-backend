package pl.memleak.panel.bll.services;

import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.exceptions.OperationNotPermittedException;

import java.util.List;

/**
 * Created by maxmati on 12/22/16
 */
public class GroupsService implements IGroupsService {

    private final ILdapDao ldapDao;
    private final IUsersService usersService;

    public GroupsService(ILdapDao ldapDao, IUsersService usersService) {
        this.ldapDao = ldapDao;
        this.usersService = usersService;
    }

    @Override
    public List<Group> getAllGroups(String authorizationUser) {
        if(!usersService.isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        return ldapDao.getAllGroups();
    }

    @Override
    public void createGroup(Group group, String authorizationUser) {
        if(!usersService.isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        ldapDao.createGroup(group);
    }

    @Override
    public void deleteGroup(String groupname, String authorizationUser) {
        if(!usersService.isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        ldapDao.deleteGroup(groupname);
    }

    @Override
    public void addToGroup(String groupname, String username, String authorizationUser) {
        if(!usersService.isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        ldapDao.addToGroup(groupname, username);
    }

    @Override
    public void removeFromGroup(String groupname, String username, String authorizationUser) {
        if(!usersService.isAdmin(authorizationUser)) throw new OperationNotPermittedException();

        ldapDao.removeFromGroup(groupname, username);
    }
}
