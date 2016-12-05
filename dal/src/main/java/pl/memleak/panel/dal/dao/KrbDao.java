package pl.memleak.panel.dal.dao;

import pl.memleak.panel.bll.dao.IKrbDao;

/**
 * Created by maxmati on 11/30/16
 */
public class KrbDao implements IKrbDao{
    @Override
    public void createPrincipal(String username) {
        //TODO: implement
    }

    @Override
    public String getRealm() {
        //TODO: implement
        return "MEMLEAK.PL";
    }

    @Override
    public void deletePrincipal(String username) {
        //TODO: implement
    }
}
