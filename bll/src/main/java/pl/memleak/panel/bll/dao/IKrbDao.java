package pl.memleak.panel.bll.dao;

/**
 * Created by maxmati on 11/30/16
 */
public interface IKrbDao {
    void createPrincipal(String username, String password) throws KrbException;

    String getRealm() throws KrbException;

    void deletePrincipal(String username) throws KrbException;

    void changePassword(String username, String password) throws KrbException;
}
