package pl.memleak.panel.dal.dto;

import org.ldaptive.beans.Attribute;
import org.ldaptive.beans.Entry;

/**
 * Created by Kamil on 19.11.2016.
 */
@Entry(dn = "distinguishedName",
attributes = {
        @Attribute(name = "uid", property = "username"),
        @Attribute(name = "cn", property = "cn"),
        @Attribute(name = "givenName", property = "givenName"),
        @Attribute(name = "sn", property = "sn"),
        @Attribute(name = "displayName", property = "displayName"),
        @Attribute(name = "mail", property = "email"),
        @Attribute(name = "userPassword", property = "userPassword"),
        @Attribute(name = "objectClass", values = {"organizationalPerson", "inetOrgPerson"})
})
public class LdapUser {

    private String username;
    private String cn;
    private String givenName;
    private String sn;
    private String distinguishedName;
    private String email;
    private String displayName;
    private String userPassword;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
