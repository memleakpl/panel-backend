package pl.memleak.panel.presentation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wigdis on 10.12.16.
 */
@RequestMapping("/api/")
@RestController
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public void login(){
        //TODO implement controller
    }
}
