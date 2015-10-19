package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.EmailService;
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

import java.util.List;


/**
 * Created by Vikranth on 10/18/2015.
 */
@Controller
public class ManagerController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("externalUserValidator")
    private Validator externalUserValidator;

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
     * @return view
     */
    @RequestMapping(value = "/manage/user/add",
            method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") AppUser user,
                          Model model) {
        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);

        return "manage/user-add";
    }


    /**
     * Get all external Users
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/manage/user/list", method = RequestMethod.GET)
    public String getExternalUsers(Model model) {
        List<AppUser> users = userService.getExternalUsers();

        LOGGER.info("Users size: " + users.size());
        model.addAttribute("users", users);
        model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);
        return "manage/user-list";
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
        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);

        // Set the defaults

        user.setTempPassword(AppUtil.getRandomPwd());
        user.setPassword(user.getTempPassword());
        user.setConfirmPassword(user.getConfirmPassword());
        // Validate all the details

        LOGGER.info("User: " + user);

        externalUserValidator.validate(user, result);

        if (result.hasErrors()) {
            LOGGER.debug("Errors in appuser obj");
            return "manage/user-add";
        }

        AppUser prevUser = userService.getUser(user.getUserId());

        if (null != prevUser) {
            String error = "User with username '" +
                    prevUser.getUserId() + "' already exists";
            LOGGER.debug(error);
            result.rejectValue("userId", "userId", error);
            return "manage/user-add";
        }

        // Everythings good
        userService.addUser(user);
        LOGGER.info("Object saved: " + user);

        // Send an email
        String sub = "You are new User account created";
        String body = "Please find the login details below" +
                "\n\nUsername: " + user.getUserId() + "\n" +
                "Password: " + user.getTempPassword();
        emailService.sendEmail(user.getEmail(), sub, body);

        PageViewBean page = new PageViewBean();
        page.setValid(true);
        page.setMessage("User created with username '" + user.getUserId() + "'");
        model.addAttribute("page", page);

        return "manage/message";
    }
}
