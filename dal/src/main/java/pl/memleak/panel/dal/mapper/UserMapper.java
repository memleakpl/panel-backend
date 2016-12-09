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
        user.setFirstName(ldapUser.getGivenName());
        user.setEmail(ldapUser.getEmail());
        user.setLastName(ldapUser.getSn());
        return user;
    }

    public static LdapUser toLdapUser(User user, String krbDomain, String peopleBaseDn){
        final String fullName = user.getFirstName() + " " + user.getLastName();

        LdapUser ldapUser = new LdapUser();
        ldapUser.setUsername(user.getUsername());
        ldapUser.setEmail(user.getEmail());
        ldapUser.setCn(fullName);
        ldapUser.setDisplayName(fullName);
        ldapUser.setGivenName(user.getFirstName());
        ldapUser.setSn(user.getLastName());
        ldapUser.setUserPassword(String.format(USER_PASSWORD_FORMAT, user.getUsername(), krbDomain));
        ldapUser.setDistinguishedName(String.format(USER_DN_FORMAT, user.getUsername(), peopleBaseDn));
        return ldapUser;
    }

}