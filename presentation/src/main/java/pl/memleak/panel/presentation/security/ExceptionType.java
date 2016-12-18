package pl.memleak.panel.presentation.security;

import org.springframework.http.HttpStatus;

/**
 * Created by wigdis on 18.12.16.
 */
public enum ExceptionType {

    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED.value()),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value()),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value()),
    INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR.value());


    private int code;

    ExceptionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
