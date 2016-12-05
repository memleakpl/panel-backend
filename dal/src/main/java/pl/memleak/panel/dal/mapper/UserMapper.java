package pl.memleak.panel.dal.mapper;

import pl.memleak.panel.bll.dto.User;
import pl.memleak.panel.dal.dto.LdapUser;

/**
 * Created by maxmati on 11/29/16
 */
public class UserMapper {

    private static final String USER_PASSWORD_FORMAT = "{sasl}%s@%s";
    private static final String USER_DN_FORMAT = "uid=%s,%s";

    public static User toUser(LdapUser ldapUser){
        User user = new User();
        user.setUsername(ldapUser.getUsername());
        user.setFullName(ldapUser.getDisplayName());
        user.setEmail(ldapUser.getEmail());
        user.setSecondName(ldapUser.getSn());
        return user;
    }

    public static LdapUser toLdapUser(User user, String krbDomain, String peopleBaseDn){
        LdapUser ldapUser = new LdapUser();
        ldapUser.setUsername(user.getUsername());
        ldapUser.setEmail(user.getEmail());
        ldapUser.setCn(user.getFullName());
        ldapUser.setDisplayName(user.getFullName());
        ldapUser.setSn(user.getSecondName());
        ldapUser.setUserPassword(String.format(USER_PASSWORD_FORMAT, user.getUsername(), krbDomain));
        ldapUser.setDistinguishedName(String.format(USER_DN_FORMAT, user.getUsername(), peopleBaseDn));
        return ldapUser;
    }

}