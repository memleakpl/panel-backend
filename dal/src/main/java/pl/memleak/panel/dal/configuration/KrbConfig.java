package pl.memleak.panel.dal.configuration;

public class KrbConfig {
    private final String jniAbsolutePath;
    private final String principal;
    private final String keytab;

    public KrbConfig(String jniAbsolutePath, String principal, String keytab) {
        this.jniAbsolutePath = jniAbsolutePath;
        this.principal = principal;
        this.keytab = keytab;
    }

    public String getJniAbsolutePath() {
        return jniAbsolutePath;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getKeytab() {
        return keytab;
    }
}
