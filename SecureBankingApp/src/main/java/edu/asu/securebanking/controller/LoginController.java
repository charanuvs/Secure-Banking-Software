package edu.asu.securebanking.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.securebanking.beans.LoginFormBean;


/**
 * @author Vikranth
 * 
 *         Login controller
 */

@Controller
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
	
	/**
	 * @param login
	 * @param result 
	 * @return login-page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(LoginFormBean login,
			BindingResult result) {

        LOGGER.info("Login bean: " + login);
        return "login/login";
    }
}
