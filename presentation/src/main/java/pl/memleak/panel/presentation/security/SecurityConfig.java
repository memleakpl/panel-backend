package pl.memleak.panel.presentation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wigdis on 04.12.16.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter();
        authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
        authFilter.setAuthenticationManager(authenticationManagerBean());
        authFilter.setAuthenticationSuccessHandler((request, response, authentication) -> response.setStatus(204));
        authFilter.setAuthenticationFailureHandler(
                (request, response, exception) -> response.setStatus(401));
        authFilter.setUsernameParameter("username");
        authFilter.setPasswordParameter("password");
        return authFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource().url("ldaps://braintest.memleak.pl/dc=memleak,dc=pl");
    }

}