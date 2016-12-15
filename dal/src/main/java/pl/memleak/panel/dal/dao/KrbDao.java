package pl.memleak.panel.dal.dao;

import pl.memleak.krbadmin.KrbAdmin;
import pl.memleak.krbadmin.KrbAdminException;
import pl.memleak.krbadmin.kadm5.Kadm5;
import pl.memleak.panel.bll.dao.IKrbDao;
import pl.memleak.panel.bll.dao.KrbException;
import pl.memleak.panel.dal.configuration.KrbConfig;

/**
 * Created by maxmati on 11/30/16
 */
public class KrbDao implements IKrbDao {
    private final KrbAdmin krbAdmin;

    public KrbDao(KrbConfig krbConfig) {
        krbAdmin = new Kadm5(krbConfig.getJniAbsolutePath(), krbConfig.getPrincipal(),
                krbConfig.getKeytab());
    }

    @Override
    public void createPrincipal(String username, String password) throws KrbException {
        try {
            krbAdmin.createPrincipal(username, password);
        } catch (KrbAdminException e) {
            throw new KrbException("Failed to create principal", e);
        }
    }

    @Override
    public String getRealm() throws KrbException {
        try {
            return krbAdmin.getRealm();
        } catch (KrbAdminException e) {
            throw new KrbException("Failed to get realm", e);
        }
    }

    @Override
    public void deletePrincipal(String username) throws KrbException {
        try {
            krbAdmin.deletePrincipal(username);
        } catch (KrbAdminException e) {
            throw new KrbException("Failed to delete principal", e);
        }
    }
}
