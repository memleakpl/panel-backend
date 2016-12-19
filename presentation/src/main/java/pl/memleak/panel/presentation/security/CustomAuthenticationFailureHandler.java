package pl.memleak.panel.presentation.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wigdis on 18.12.16.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    static private Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if ((exception instanceof InternalAuthenticationServiceException)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (exception instanceof BadRequestAuthenticationException) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }
}
