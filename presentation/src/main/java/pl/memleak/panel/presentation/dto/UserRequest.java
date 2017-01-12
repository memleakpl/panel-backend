package pl.memleak.panel.presentation.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by maxmati on 1/12/17
 */
public class UserRequest {

    @NotNull
    @Pattern(regexp = "^[a-z0-9]+$")
    private String username;
    @NotNull
    @Pattern(regexp = "^[a-z0-9ęóąśłżźćń-]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String firstName;
    @NotNull
    @Pattern(regexp = "^([\\w.+-]+)@([\\w-]+\\.)*([\\w-]+)$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
    @NotNull
    @Pattern(regexp = "^[a-z0-9ęóąśłżźćń-]+$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }
}
