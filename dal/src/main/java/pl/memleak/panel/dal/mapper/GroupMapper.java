package pl.memleak.panel.dal.mapper;

import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.dal.dto.LdapGroup;

/**
 * Created by maxmati on 12/22/16
 */
public class GroupMapper {

    public static Group toGroup(LdapGroup ldapGroup){
        return new Group(
                ldapGroup.getName(),
                ldapGroup.getOwner(),
                ldapGroup.getDescription()
        );
    }
}
