package pl.memleak.panel.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by maxmati on 12/22/16
 */
@RestController
@RequestMapping("/api/session")
public class SessionController {

    @RequestMapping(method = RequestMethod.GET, value = "")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void isLoggedIn(){}
}
