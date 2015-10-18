package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by Vikranth on 10/17/2015.
 */
@Controller
public class UserController {

    private static Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping(value = {"/user/home", "/user/"}, method = RequestMethod.GET)
    public String home(HttpSession session,
                       Model model) {
        AppUser user = (AppUser) session.getAttribute("user");

        model.addAttribute("user", user.getEmail());
        return "user/home";
    }

}
