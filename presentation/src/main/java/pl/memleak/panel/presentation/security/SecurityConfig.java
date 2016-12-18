package pl.memleak.panel.presentation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.memleak.panel.presentation.configuration.LoginConfig;

/**
 * Created by wigdis on 04.12.16.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LoginConfig loginConfig;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    public SecurityConfig(@Qualifier("loginConfig") LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter();
        authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
        authFilter.setAuthenticationManager(authenticationManagerBean());
        authFilter.setAuthenticationSuccessHandler((request, response, authentication) -> response.setStatus(204));
        authFilter.setAuthenticationFailureHandler(
                (request, response, exception) -> response.setStatus(401));
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
                .userSearchBase(loginConfig.getUserSearchBase())
                .userSearchFilter(loginConfig.getUserSearchFilter())
                .contextSource().url(loginConfig.getUrl());
    }

}