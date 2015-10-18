package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.AppUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by Vikranth on 10/18/2015.
 */
@Controller
public class ManagerController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUserValidator;

    private static Logger LOGGER = Logger.getLogger(ManagerController.class);

    /**
     * Manager home page
     *
     * @return home
     */
    @RequestMapping(value = {"/manage/home", "/manage/"},
            method = RequestMethod.GET)

    public String home() {
        return "manage/home";
    }

    /**
     * Add user form
     *
     * @return add-user
     */
    @RequestMapping(value = "/manage/user/add",
            method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") AppUser user,
                          Model model) {
        model.addAttribute("genders", AppConstants.GENDERS);
        return "manage/add-user";
    }

    /**
     * @param user
     * @param model
     * @param result
     * @return view
     */
    @RequestMapping(value = "/manage/user/add",
            method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") AppUser user,
                          Model model,
                          BindingResult result) {
        // Set the defaults
        user.setUserType(AppConstants.ROLE_NORMAL);

        user.setTempPassword(AppUtil.getRandomPwd());
        user.setPassword(user.getTempPassword());
        user.setConfirmPassword(user.getConfirmPassword());
        // Validate all the details

        appUserValidator.validate(user, result);

        if (result.hasErrors()) {
            LOGGER.debug("Errors in appuser obj");
            model.addAttribute("genders", AppConstants.GENDERS);
            return "manage/add-user";
        }

        AppUser prevUser = userService.getUser(user.getUserId());

        if (null != prevUser) {
            String error = "User with username '" +
                    prevUser.getUserId() + "' already exists";
            LOGGER.debug(error);
            model.addAttribute("genders", AppConstants.GENDERS);
            result.rejectValue("userId", "userId", error);
            return "manage/add-user";
        }

        // Everythings good
        userService.addUser(user);
        LOGGER.info("Object saved: " + user);

        PageViewBean page = new PageViewBean();
        page.setValid(true);
        page.setMessage("User created with username '" + user.getUserId() + "'");
        model.addAttribute("page", page);
        // Check if user exists
        return "manage/message";
    }
}
