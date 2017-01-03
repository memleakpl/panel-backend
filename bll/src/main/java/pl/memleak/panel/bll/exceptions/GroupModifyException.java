package pl.memleak.panel.bll.exceptions;

/**
 * Created by oszust on 03.01.17.
 */
public class GroupModifyException extends RuntimeException {
    public GroupModifyException() {
        super();
    }

    public GroupModifyException(String message) {
        super(message);
    }

    public GroupModifyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GroupModifyException(Throwable throwable) {
        super(throwable);
    }

    protected GroupModifyException(String message, Throwable throwable, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }
}
