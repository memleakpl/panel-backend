package pl.memleak.panel.bll.dao;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by wigdis on 29.01.17.
 */
public interface IResetTokenDao {
    void addToken(String username, String token, LocalDateTime dueDate);
    Optional<String> getToken(String username, LocalDateTime dueDate);
}
