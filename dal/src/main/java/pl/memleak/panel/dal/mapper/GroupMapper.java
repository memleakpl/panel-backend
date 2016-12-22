package pl.memleak.panel.dal.mapper;

import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.dal.dto.LdapGroup;
import pl.memleak.panel.dal.dto.LdapUser;

/**
 * Created by maxmati on 12/22/16
 */
public class GroupMapper {

    private static final String GROUP_DN_FORMAT = "cn=%s,%s";

    public static Group toGroup(LdapGroup ldapGroup){
        return new Group(
                ldapGroup.getName(),
                ldapGroup.getOwner(),
                ldapGroup.getDescription()
        );
    }

    public static LdapGroup toLdapGroup(Group group, LdapUser owner, String defaultGroupBaseDn) {
        return new LdapGroup(
                String.format(GROUP_DN_FORMAT, group.getName(), defaultGroupBaseDn),
                group.getName(),
                owner.getDistinguishedName(),
                group.getDescription()
        );
    }
}
