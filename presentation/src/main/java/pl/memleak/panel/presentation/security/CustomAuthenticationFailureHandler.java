package pl.memleak.panel.presentation.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        ApplicationAuthenticationException authenticationException =
                (ApplicationAuthenticationException) exception;

        response.setStatus(authenticationException.getExceptionType().getCode());
        response.getWriter().write(authenticationException.toJSONFormat());
        response.getWriter().flush();
        response.getWriter().close();

        logger.info("Authentication failed", exception);
    }
}
