package edu.asu.securebanking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Vikranth on 10/17/2015.
 */
@Controller
public class UserController {

    @RequestMapping(value = {"/user/home", "/user/"}, method = RequestMethod.GET)
    public String home() {
        return "user/home";
    }
}
