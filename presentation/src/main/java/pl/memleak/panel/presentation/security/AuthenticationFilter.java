package pl.memleak.panel.presentation.security;

import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.memleak.panel.presentation.dto.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

//@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginRequest loginRequest = this.getLoginRequest(request);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {

        BufferedReader reader = null;
        LoginRequest loginRequest = null;

        try {
            reader = request.getReader();
            Gson gson = new Gson();
            loginRequest = gson.fromJson(reader, LoginRequest.class);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }

        if (loginRequest == null) {
            loginRequest = new LoginRequest();
        }
        return loginRequest;
    }
}
