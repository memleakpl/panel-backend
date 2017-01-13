package pl.memleak.panel.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by maxmati on 1/12/17
 */
public class GroupRequest {
    @NotNull
    @Pattern(regexp = "^[a-z0-9ęóąśłżźćń-]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String description;
    @NotNull
    @Pattern(regexp = "^[a-z0-9]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String owner;
    @NotNull
    @Pattern(regexp = "^[a-z0-9]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String name;

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
