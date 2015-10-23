package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.LoginFormBean;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.LoginService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.LoginFormValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


/**
 * @author Vikranth
 *         <p>
 *         Login controller
 */

@Controller
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    @Autowired
    @Qualifier("loginFormValidator")
    LoginFormValidator loginFormValidator;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    /**
     * @param login
     * @return login-page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(LoginFormBean login, @RequestParam(value = "invalidCred",
            required = false) String invalidCred, Model model,
                        HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        // If loggedIn then redirect
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        if (user != null) {
            String redirect = AppConstants.ROLE_URL_MAP.get(user.getUserType());
            return "redirect:/" + redirect + "/";
        }

        if (null != invalidCred &&
                Boolean.TRUE.toString().equalsIgnoreCase(invalidCred)) {
            page.setValid(false);
        }

        return "login/login";
    }
}
