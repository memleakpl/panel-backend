package pl.memleak.panel.dal.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by wigdis on 15.01.17.
 */
@Entity
public class DbResetToken {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    private String userDn;
    private LocalDateTime expirationDate;

    public DbResetToken() {
    }

    public DbResetToken(String token, String userDn, LocalDateTime expirationDate) {
        this.token = token;
        this.userDn = userDn;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserDn() {
        return userDn;
    }

    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
