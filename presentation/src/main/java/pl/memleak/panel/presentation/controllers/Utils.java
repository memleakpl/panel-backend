package pl.memleak.panel.presentation.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by maxmati on 1/12/17
 */
class Utils {
    static String getCurrentUser() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        if (principal == null)
            throw new RuntimeException("Missing authentication principal");

        return principal.getName();
    }
}
