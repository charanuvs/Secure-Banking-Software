package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.LoginFormBean;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.service.LoginService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.LoginFormValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String login(LoginFormBean login, @RequestParam(value = "invalidCred",
            required = false) String invalidCred, Model model,
                        BindingResult result) {

        LOGGER.info("Login bean: " + login);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        LOGGER.info("Credentials");
        if (null != invalidCred &&
                Boolean.TRUE.toString().equalsIgnoreCase(invalidCred)) {
            LOGGER.info("Invalid credentials supplied");
            page.setValid(false);
        }

        return "login/login";
    }
}
