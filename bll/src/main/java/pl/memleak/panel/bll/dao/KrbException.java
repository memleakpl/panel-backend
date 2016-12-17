package pl.memleak.panel.bll.dao;

public class KrbException extends Exception {
    public KrbException() {
        super();
    }

    public KrbException(String message) {
        super(message);
    }

    public KrbException(String message, Throwable cause) {
        super(message, cause);
    }

    public KrbException(Throwable cause) {
        super(cause);
    }

    public KrbException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
