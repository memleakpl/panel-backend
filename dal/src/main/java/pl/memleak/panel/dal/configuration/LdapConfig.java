package pl.memleak.panel.dal.configuration;

/**
 * Created by maxmati on 11/29/16
 */
public class LdapConfig {

    private String uidFilter;
    private String cnFilter;
    private String allUsersFilter;
    private String defaultUserBaseDn;
    private String defaultGroupBaseDn;
    private String allGroupsFilter;
    private String groupMemberFilter;

    public LdapConfig(
            String uidFilter, String cnFilter,
            String allUsersFilter, String allGroupsFilter,
            String groupMemberFilter, String defaultUserBaseDn,
            String defaultGroupBaseDn) {
        this.uidFilter = uidFilter;
        this.cnFilter = cnFilter;
        this.allUsersFilter = allUsersFilter;
        this.allGroupsFilter = allGroupsFilter;
        this.defaultUserBaseDn = defaultUserBaseDn;
        this.defaultGroupBaseDn = defaultGroupBaseDn;
        this.groupMemberFilter = groupMemberFilter;
    }

    public String getAllUsersFilter() {
        return allUsersFilter;
    }

    public String getUidFilter() {
        return uidFilter;
    }

    public String getCnFilter() {
        return cnFilter;
    }

    public String getDefaultUserBaseDn() {
        return defaultUserBaseDn;
    }

    public String getDefaultGroupBaseDn() {
        return defaultGroupBaseDn;
    }

    public String getAllGroupsFilter() {
        return allGroupsFilter;
    }

    public String getGroupMemberFilter() {
        return groupMemberFilter;
    }
}
