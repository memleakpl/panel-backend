package pl.memleak.panel.presentation.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by wigdis on 18.12.16.
 */
public class BadRequestAuthenticationException extends AuthenticationException {

    public BadRequestAuthenticationException(String msg, Throwable t) {
        super(msg, t);

    }

    public BadRequestAuthenticationException(String msg) {
        super(msg);

    }

}
