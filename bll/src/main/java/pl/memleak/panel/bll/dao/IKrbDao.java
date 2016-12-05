package pl.memleak.panel.bll.dao;

/**
 * Created by maxmati on 11/30/16
 */
public interface IKrbDao {
    void createPrincipal(String username);

    String getRealm();

    void deletePrincipal(String username);
}
