package pl.memleak.panel.dal.dto;

import org.ldaptive.beans.Attribute;
import org.ldaptive.beans.Entry;

import java.util.List;

/**
 * Created by Kamil on 20.11.2016.
 */


@Entry(dn = "distinguishedName",
        attributes = {
                @Attribute(name = "cn", property = "name"),
                @Attribute(name = "member", property = "members")
        })
public class LdapGroup {
    private String distinguishedName;
    private String name;
    private List<String> members;

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }


}
