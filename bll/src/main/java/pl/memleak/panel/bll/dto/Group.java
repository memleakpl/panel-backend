package pl.memleak.panel.bll.dto;

/**
 * Created by maxmati on 12/22/16
 */
public class Group {
    private final String description;
    private final String owner;
    private final String name;

    public Group(String name, String owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }
}
