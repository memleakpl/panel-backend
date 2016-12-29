package pl.memleak.panel.dal.mapper;

import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.dal.dto.LdapGroup;
import pl.memleak.panel.dal.dto.LdapUser;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

/**
 * Created by maxmati on 12/22/16
 */
public class GroupMapper {

    private static final String GROUP_DN_FORMAT = "cn=%s,%s";

    private static String getUid(String dn){
        try {
            LdapName name = new LdapName(dn);
            String rdn = name.get(name.size() - 1);
            String[] parts = rdn.split("=");
            if(parts[0].equals("uid")){
                return parts[1];
            } else {
                throw new InvalidNameException("User dn have to begins with uid");
            }
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static Group toGroup(LdapGroup ldapGroup){
        return new Group(
                ldapGroup.getName(),
                getUid(ldapGroup.getOwner()),
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
