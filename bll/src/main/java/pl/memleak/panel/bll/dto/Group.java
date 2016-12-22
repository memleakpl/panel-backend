package pl.memleak.panel.bll.dto;

/**
 * Created by maxmati on 12/22/16
 */
public class Group {
    private String description;
    private String owner;
    private String name;

    public Group() {
    }

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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }
}
