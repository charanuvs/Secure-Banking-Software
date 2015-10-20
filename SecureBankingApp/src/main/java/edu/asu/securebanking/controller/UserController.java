package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * Created by Vikranth on 10/17/2015.
 */
@Controller
public class UserController {

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUValidator;

    private static Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping(value = {"/user/home", "/user/"}, method = RequestMethod.GET)
    public String home(HttpSession session,
                       Model model) {
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);

        model.addAttribute("user", user.getEmail());
        return "user/home";
    }

    // All common functionalities

    @RequestMapping(value = {"/user/update/pwd",
            "/manage/update/pwd",
            "/merch/update/pwd",
            "/emp/update/pwd",
            "/admin/update/pwd"},
            method = RequestMethod.GET)
    public String updatePassword() {

        return null;
    }

    /**
     * @param model
     * @param session
     * @param result
     * @return view
     */
    @RequestMapping(value = {"/user/update/pwd",
            "/manage/update/pwd",
            "/merch/update/pwd",
            "/emp/update/pwd",
            "/admin/update/pwd"},
            method = RequestMethod.POST)
    public String updatePassword(Model model,
                                 HttpSession session,
                                 BindingResult result) {
        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);
        String role = loggedInUser.getUserType();

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {


        } catch (Exception e) {

        }
        return "";
    }
}
