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
import pl.memleak.panel.bll.exceptions.GroupModifyException;
import pl.memleak.panel.dal.configuration.LdapConfig;
import pl.memleak.panel.dal.dto.LdapGroup;
import pl.memleak.panel.dal.dto.LdapUser;
import pl.memleak.panel.bll.exceptions.EntityNotFoundException;
import pl.memleak.panel.dal.mapper.GroupMapper;
import pl.memleak.panel.dal.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LdapDao implements ILdapDao {

    private final ConnectionFactory connectionFactory;
    private final LdapConfig ldapConfig;

    public LdapDao(ConnectionFactory connectionFactory, LdapConfig ldapConfig) {
        this.connectionFactory = connectionFactory;
        this.ldapConfig = ldapConfig;
    }

    @Override
    public User getUser(String username) {
        return getUser(ldapConfig.getDefaultUserBaseDn(), username);
    }

    @Override
    public User getUser(String baseDn, String username) {
        LdapUser ldapUser = getRawUser(baseDn, username);
        return UserMapper.toUser(ldapUser);
    }

    private LdapUser getRawUser(String username) {
        return getRawUser(ldapConfig.getDefaultUserBaseDn(), username);
    }

    private LdapUser getRawUser(String baseDn, String username) {
        SearchFilter userFilter = new SearchFilter(ldapConfig.getUidFilter());
        userFilter.setParameter("uid", username);
        return query(baseDn, userFilter, LdapUser.class).stream()
                .findFirst().orElseThrow(() -> new EntityNotFoundException("User " + username + " was not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return getAllUsers(ldapConfig.getDefaultUserBaseDn());
    }

    @Override
    public List<User> getAllUsers(String baseDn) {
        SearchFilter userFilter = new SearchFilter(ldapConfig.getAllUsersFilter());
        List<LdapUser> ldapUsers = query(baseDn, userFilter, LdapUser.class);
        return ldapUsers.stream().map(UserMapper::toUser).collect(Collectors.toList());
    }

    @Override
    public List<Group> getAllGroups() {
        return getAllGroups(ldapConfig.getDefaultGroupBaseDn());
    }

    @Override
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

        createEntry(ldapEntry);

    }

    @Override
    public void createGroup(Group group) {
        LdapGroup ldapGroup = GroupMapper.toLdapGroup(
                group, this.getRawUser(group.getOwner()),
                ldapConfig.getDefaultGroupBaseDn());

        DefaultLdapEntryMapper<LdapGroup> mapper = new DefaultLdapEntryMapper<>();
        LdapEntry ldapEntry = mapper.map(ldapGroup);

        createEntry(ldapEntry);
    }

    @Override
    public void editUser(User user) {
        LdapUser oldLdapUser = getRawUser(user.getUsername());
        LdapUser newLdapUser = UserMapper.ldapUserUpdate(user, oldLdapUser);

        AttributeModification[] attributeModifications = createUserModificationsArray(newLdapUser, oldLdapUser);

        if (attributeModifications.length == 0) return;

        try (Connection connection = connectionFactory.getConnection()) {
            connection.open();

            ModifyRequest modifyRequest = new ModifyRequest(newLdapUser.getDistinguishedName(),
                    attributeModifications);

            ModifyOperation modify = new ModifyOperation(connection);
            modify.execute(modifyRequest);
        } catch (LdapException e) {
            throw new RuntimeException("Unable to execute LDAP modify command", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try {
            LdapUser toDelete = getRawUser(username);
            deleteEntity(toDelete.getDistinguishedName());
        } catch (EntityNotFoundException e) {
            //ignore user already deleted
        }
    }

    private void deleteEntity(String dn) {
        try(Connection conn = connectionFactory.getConnection()) {
            conn.open();
            DeleteOperation delete = new DeleteOperation(conn);
            delete.execute(new DeleteRequest(dn));
        } catch (LdapException e) {
            throw new RuntimeException("Unable to execute LDAP delete command", e);
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
        try {
            LdapGroup toDelete = getRawGroup(groupname);
            deleteEntity(toDelete.getDistinguishedName());
        } catch (EntityNotFoundException e) {
            //ignore user already deleted
        }
    }

    @Override
    public void editGroup(Group group) {
        LdapUser newOwner = getRawUser(group.getOwner());
        LdapGroup oldLdapGroup = getRawGroup(group.getName());
        LdapGroup newLdapGroup = GroupMapper.ldapGroupUpdate(group, newOwner, oldLdapGroup);


        AttributeModification[] attributeModifications = createGroupModificationsArray(newLdapGroup, oldLdapGroup);

        if (attributeModifications.length == 0) return;

        try (Connection connection = connectionFactory.getConnection()) {
            connection.open();

            ModifyRequest modifyRequest = new ModifyRequest(newLdapGroup.getDistinguishedName(),
                    attributeModifications);

            ModifyOperation modify = new ModifyOperation(connection);
            modify.execute(modifyRequest);
        } catch(LdapException e){
            throw new GroupModifyException("Cannot edit group", e);
        }
    }

    @Override
    public void addToGroup(String groupname, String username) {
        LdapGroup group = getRawGroup(groupname);
        LdapUser user = getRawUser(username);

        try {
            editGroupMembership(group, user, AttributeModificationType.ADD);
        } catch (LdapException e) {
            throw new GroupModifyException("Cannot add user to group", e);
        }
    }

    @Override
    public void removeFromGroup(String groupname, String username) {
        LdapGroup group = getRawGroup(groupname);
        LdapUser user = getRawUser(username);

        List<String> newGroupMembers = group.getMembers();
        newGroupMembers.remove(user.getDistinguishedName());
        if(newGroupMembers.isEmpty()){
            deleteEntity(group.getDistinguishedName());
            return;
        }
        try {
            editGroupMembership(group, user, AttributeModificationType.REMOVE);
        } catch (LdapException e) {
            throw new GroupModifyException("Cannot delete user from group", e);
        }
    }

    @Override
    public List<Group> getUserGroups(String username) {
        LdapUser user = getRawUser(username);
        SearchFilter groupMemberFilter = new SearchFilter(ldapConfig.getGroupMemberFilter());
        groupMemberFilter.setParameter("dn", user.getDistinguishedName());
        List<LdapGroup> groups = query(ldapConfig.getDefaultGroupBaseDn(), groupMemberFilter, LdapGroup.class);
        return groups.stream().map(GroupMapper::toGroup).collect(Collectors.toList());
    }

    @Override
    public User getUserByDn(String dn) {
        final SearchFilter filter = new SearchFilter(ldapConfig.getAllUsersFilter());
        Optional<LdapUser> user = query(dn, filter, LdapUser.class).stream().findFirst();
        return user.map(UserMapper::toUser).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public String getUserDn(String username) {
        return getRawUser(username).getDistinguishedName();
    }

    @Override
    public Group getGroup(String baseDn, String groupname){
        LdapGroup ldapGroup = getRawGroup(baseDn, groupname);
        return GroupMapper.toGroup(ldapGroup);
    }

    @Override
    public Group getGroup(String groupname){
        return getGroup(ldapConfig.getDefaultGroupBaseDn(), groupname);
    }

    private void editGroupMembership(LdapGroup group, LdapUser user, AttributeModificationType operationType)
            throws LdapException {
        try(Connection conn = connectionFactory.getConnection()) {
            conn.open();
            AttributeModification attributeModification = new AttributeModification(
                    operationType,
                    new LdapAttribute("member", user.getDistinguishedName()));
            ModifyRequest modifyRequest = new ModifyRequest(group.getDistinguishedName(), attributeModification);
            ModifyOperation modifyOperation = new ModifyOperation(conn);
            modifyOperation.execute(modifyRequest);
        }
    }

    private LdapGroup getRawGroup(String groupname) {
        return getRawGroup(ldapConfig.getDefaultGroupBaseDn(), groupname);
    }
    private LdapGroup getRawGroup(String baseDn, String groupname) {
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

    private void createEntry(LdapEntry ldapEntry) {
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

    private AttributeModification createAttributeModification(String attribute, String value){
        return new AttributeModification(AttributeModificationType.REPLACE,
                new LdapAttribute(attribute, value));
    }

    private AttributeModification[] createGroupModificationsArray(LdapGroup newLdapGroup, LdapGroup oldLdapGroup){

        List<AttributeModification> modsList = new ArrayList<>();

        if( !newLdapGroup.getDescription().equals(oldLdapGroup.getDescription())){
            modsList.add(createAttributeModification("description", newLdapGroup.getDescription()));
        }

        if( !newLdapGroup.getOwner().equals(oldLdapGroup.getOwner())){
            modsList.add(createAttributeModification("owner", newLdapGroup.getOwner()));
        }

        return modsList.toArray(new AttributeModification[modsList.size()]);
    }

    private AttributeModification[] createUserModificationsArray(LdapUser newLdapUser, LdapUser oldLdapUser){

        List<AttributeModification> modsList = new ArrayList<>();

        if( !newLdapUser.getEmail().equals(oldLdapUser.getEmail())){
            modsList.add(createAttributeModification("mail", newLdapUser.getEmail()));
        }

        if( !newLdapUser.getGivenName().equals(oldLdapUser.getGivenName())){
            modsList.add(createAttributeModification("givenName", newLdapUser.getGivenName()));
        }

        if( !newLdapUser.getSn().equals(oldLdapUser.getSn())){
            modsList.add(createAttributeModification("sn", newLdapUser.getSn()));
        }

        if( !newLdapUser.getCn().equals(oldLdapUser.getCn())){
            modsList.add(createAttributeModification("cn", newLdapUser.getCn()));
        }

        if( !newLdapUser.getDisplayName().equals(oldLdapUser.getDisplayName())){
            modsList.add(createAttributeModification("displayName", newLdapUser.getDisplayName()));
        }

        return modsList.toArray(new AttributeModification[modsList.size()]);
    }
}
