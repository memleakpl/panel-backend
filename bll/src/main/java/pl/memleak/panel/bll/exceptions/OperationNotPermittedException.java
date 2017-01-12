package pl.memleak.panel.bll.exceptions;

/**
 * Created by maxmati on 1/12/17
 */
public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException() {
        super();
    }

    public OperationNotPermittedException(String message) {
        super(message);
    }

    public OperationNotPermittedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotPermittedException(Throwable cause) {
        super(cause);
    }

    protected OperationNotPermittedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
