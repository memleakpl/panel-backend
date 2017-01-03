package pl.memleak.panel.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by oszust on 03.01.17.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ModifyException extends RuntimeException {
    public ModifyException() {
        super();
    }

    public ModifyException(String message) {
        super(message);
    }

    public ModifyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ModifyException(Throwable throwable) {
        super(throwable);
    }

    protected ModifyException(String message, Throwable throwable, boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }
}
