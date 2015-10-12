package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.LoginFormBean;
import edu.asu.securebanking.service.LoginService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.LoginFormValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author Vikranth
 *         <p>
 *         Login controller
 */

@Controller
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @Autowired
    LoginFormValidator loginFormValidator;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    /**
     * @param login
     * @param result
     * @return login-page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(LoginFormBean login, BindingResult result) {

        LOGGER.info("Login bean: " + login);

        AppUser user = userService.getUser("vdoosa@asu.edu");
        LOGGER.info("User: " + user);
        return "login/login";
    }
}
