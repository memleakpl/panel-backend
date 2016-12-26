package pl.memleak.panel.dal.dao;

import org.ldaptive.*;
import org.ldaptive.auth.AuthenticationRequest;
import org.ldaptive.auth.Authenticator;
import org.ldaptive.auth.BindAuthenticationHandler;
import org.ldaptive.auth.SearchDnResolver;
import org.ldaptive.beans.reflect.DefaultLdapEntryMapper;
import pl.memleak.panel.bll.dao.ILdapDao;
import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.dal.configuration.LdapConfig;
import pl.memleak.panel.dal.dto.LdapGroup;
import pl.memleak.panel.dal.dto.LdapUser;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.dal.mapper.GroupMapper;
import pl.memleak.panel.dal.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class LdapDao implements ILdapDao {

    private static final String DELETE_USER_REQUEST_FORMAT = "uid=%s,%s";
    private final ConnectionFactory connectionFactory;
    private final LdapConfig ldapConfig;

    public LdapDao(ConnectionFactory connectionFactory, LdapConfig ldapConfig) {
        this.connectionFactory = connectionFactory;
        this.ldapConfig = ldapConfig;
    }

    public User getUser(String username) {
        return getUser(ldapConfig.getDefaultUserBaseDn(), username);
    }

    public User getUser(String baseDn, String username) {
        SearchFilter userFilter = new SearchFilter(ldapConfig.getUidFilter());
        userFilter.setParameter("uid", username);
        LdapUser ldapUser = query(baseDn, userFilter, LdapUser.class).stream()
                .findFirst().orElseThrow(() -> new RuntimeException("User " + username + " was not found"));
        return UserMapper.toUser(ldapUser);
    }

    public List<User> getAllUsers() {
        return getAllUsers(ldapConfig.getDefaultUserBaseDn());
    }

    public List<User> getAllUsers(String baseDn) {
        SearchFilter userFilter = new SearchFilter(ldapConfig.getAllUsersFilter());
        List<LdapUser> ldapUsers = query(baseDn, userFilter, LdapUser.class);
        return ldapUsers.stream().map(UserMapper::toUser).collect(Collectors.toList());
    }

    public List<Group> getAllGroups() {
        return getAllGroups(ldapConfig.getDefaultGroupBaseDn());
    }

    public List<Group> getAllGroups(String baseDn) {
        SearchFilter groupFilter = new SearchFilter(ldapConfig.getAllGroupsFilter());
        List<LdapGroup> ldapGroups = query(baseDn, groupFilter, LdapGroup.class);
        return ldapGroups.stream().map(GroupMapper::toGroup).collect(Collectors.toList());
    }

    @Override
    public void createUser(User user, String realm) {
        LdapUser ldapUser = UserMapper.toLdapUser(user, realm, ldapConfig.getDefaultUserBaseDn());
        DefaultLdapEntryMapper<LdapUser> mapper = new DefaultLdapEntryMapper<>();
        LdapEntry ldapEntry = mapper.map(ldapUser);

        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            connection.open();

            AddOperation add = new AddOperation(connection);
            add.execute(new AddRequest(ldapEntry.getDn(), ldapEntry.getAttributes()));
        } catch (LdapException e) {
            throw new RuntimeException("Unable to execute LDAP add command", e);
        } finally {
            if (connection != null) connection.close();
        }

    }

    @Override
    public void deleteUser(String username) {
        Connection conn = null;
        try {
            conn = connectionFactory.getConnection();
            conn.open();
            DeleteOperation delete = new DeleteOperation(conn);
            delete.execute(new DeleteRequest(String.format(DELETE_USER_REQUEST_FORMAT, username, ldapConfig.getDefaultUserBaseDn())));
        } catch (LdapException e) {
            throw new RuntimeException("Unable to execute LDAP delete command", e);
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            SearchFilter uidFilter = new SearchFilter(ldapConfig.getUidFilter());
            SearchDnResolver searchDnResolver = new SearchDnResolver(connectionFactory);

            uidFilter.setParameter("uid", username);

            searchDnResolver.setBaseDn(ldapConfig.getDefaultUserBaseDn());
            searchDnResolver.setUserFilter(uidFilter.format());

            return new Authenticator(searchDnResolver, new BindAuthenticationHandler(connectionFactory)).authenticate(
                    new AuthenticationRequest(username, new Credential(password))).getResult();
        } catch (LdapException e) {
            throw new RuntimeException("LDAP authentication failed", e);
        }
    }

    @Override
    public void deleteGroup(String groupname) {
        LdapGroup toDelete = getGroup(groupname);

        Connection conn = null;
        try {
            conn = connectionFactory.getConnection();
            conn.open();
            DeleteOperation delete = new DeleteOperation(conn);
            delete.execute(new DeleteRequest(toDelete.getDistinguishedName()));
        } catch (LdapException e) {
            throw new RuntimeException("Unable to execute LDAP delete command", e);
        } finally {
            if (conn != null) conn.close();
        }
    }

    public LdapGroup getGroup(String groupname) {
        return getGroup(ldapConfig.getDefaultGroupBaseDn(), groupname);
    }

    public LdapGroup getGroup(String baseDn, String groupname) {
        SearchFilter groupFilter = new SearchFilter(ldapConfig.getCnFilter());
        groupFilter.setParameter("cn", groupname);
        return query(baseDn, groupFilter, LdapGroup.class).stream()
                .findFirst().orElseThrow(() -> new EntityNotFoundException("Group " + groupname + " was not found"));
    }

    private <T> List<T> query(String baseDn, SearchFilter filter, Class<T> targetClass) {
        SearchExecutor executor = new SearchExecutor();
        executor.setBaseDn(baseDn);
        try {
            SearchResult result = executor.search(connectionFactory, filter).getResult();
            DefaultLdapEntryMapper<T> mapper = new DefaultLdapEntryMapper<>();
            return result.getEntries().stream()
                    .map(entry -> mapEntry(mapper, entry, targetClass))
                    .collect(Collectors.toList());
        } catch (LdapException e) {
            throw new RuntimeException("Cannot send query to ldap", e);
        }

    }

    private <T> T mapEntry(DefaultLdapEntryMapper<T> mapper, LdapEntry entry, Class<T> targetClass) {
        T object;
        try {
            object = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Problem with creating instance of class:" + targetClass.getName(), e);
        }
        mapper.map(entry, object);
        return object;
    }
}
