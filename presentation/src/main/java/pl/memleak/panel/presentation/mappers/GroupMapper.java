package pl.memleak.panel.presentation.mappers;

import pl.memleak.panel.bll.dto.Group;
import pl.memleak.panel.presentation.dto.GroupRequest;

/**
 * Created by maxmati on 1/12/17
 */
public class GroupMapper {
    public static Group toGroup(GroupRequest groupRequest) {
        return new Group(
                groupRequest.getName(),
                groupRequest.getOwner(),
                groupRequest.getDescription());
    }
}
