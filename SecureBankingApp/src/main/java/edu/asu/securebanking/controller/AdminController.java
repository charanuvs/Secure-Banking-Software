package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.AccountService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
@Controller
public class AdminController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("internalUserValidator")
    private Validator internalUserValidator;

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUserValidator;

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("accountValidator")
    private Validator accountValidator;

    private static Logger LOGGER = Logger.getLogger(AdminController.class);

    /**
     * Admin home page
     *
     * @return home
     */
    @RequestMapping(value = {"/admin/home", "/admin/"},
            method = RequestMethod.GET)

    public String home() {
        return "admin/home";
    }

    /**
     * Add user form
     *
     * @return view
     */
    @RequestMapping(value = "/admin/user/add",
            method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") AppUser user,
                          Model model) {
        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);

        return "admin/user-add";
    }

    /**
     * Add a internal user
     *
     * @param user
     * @param model
     * @param result
     * @return view
     */
    @RequestMapping(value = "/admin/user/add",
            method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") AppUser user,
                          Model model,
                          BindingResult result) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);

        try {

            // Set the defaults
            user.setTempPassword(AppUtil.getRandomPwd());
            user.setPassword(user.getTempPassword());
            user.setConfirmPassword(user.getConfirmPassword());
            user.setStatus(AppConstants.USER_ACTIVE);
            // Validate all the details

            LOGGER.info("User: " + user);

            internalUserValidator.validate(user, result);

            if (result.hasErrors()) {
                LOGGER.debug("Errors in appuser obj");
                return "admin/user-add";
            }

            AppUser prevUser = null;

            prevUser = userService.getUser(user.getUserId());

            if (null != prevUser) {
                String error = "User with username '" +
                        prevUser.getUserId() + "' already exists";
                LOGGER.debug(error);
                result.rejectValue("userId", "userId", error);
                return "admin/user-add";
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

            page.setValid(true);
            page.setMessage("User created with username '" +
                    user.getUserId() + "'");
            model.addAttribute("page", page);

            return "message";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }

    }

    /**
     * Get all internal Users
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
    public String getInternalUsers(Model model) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            List<AppUser> users = userService.getInternalUsers();

            LOGGER.info("Users size: " + users.size());
            model.addAttribute("users", users);
            model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);
            model.addAttribute("status", AppConstants.USER_STATUS);
            return "admin/user-list";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

    /**
     * Update user form
     *
     * @param username
     * @param model
     * @return
     */
    @RequestMapping(value = "/admin/user/update/{id}",
            method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") String username,
                             Model model,
                             HttpSession session) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        String loggedInUsername = ((AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER))
                .getUserId();
        // Get user
        try {
            AppUser user = userService.getInternalUser(username);

            if (null == user) {
                page.setValid(false);
                page.setMessage("The user does not exist");
                return "message";
            } else if (loggedInUsername.equals(user.getUserId())) {
                page.setValid(false);
                page.setMessage("You cannot edit your own profile");
                return "message";
            }

            // User exists
            model.addAttribute("user", user);
            // other attributes
            model.addAttribute("genders", AppConstants.GENDERS);
            model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);
            model.addAttribute("status", AppConstants.USER_STATUS);
            return "admin/user-update";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

    /**
     * User update submit
     *
     * @param user
     * @param model
     * @param result
     * @return
     */
    @RequestMapping(value = "/admin/user/update",
            method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") AppUser user,
                             Model model,
                             BindingResult result) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);
        model.addAttribute("status", AppConstants.USER_STATUS);

        // Everything is good
        try {

            LOGGER.info("Update user: " + user);

            if (user.getUserId() == null || user.getUserId().trim().length() == 0) {
                page.setValid(false);
                page.setMessage("Invalid user update");

                return "message";
            }

            // Get the user with the username
            AppUser internalUser = null;
            try {
                internalUser = userService.getInternalUser(user.getUserId());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

            if (null == internalUser) { // not an internal user
                page.setValid(false);
                page.setMessage("Invalid user update");

                return "message";
            }

            // Copy the values to be updated
            AppUtil.copySubUserToDBUser(user, internalUser);

            // Validate the inputs
            internalUserValidator.validate(internalUser, result);

            if (result.hasErrors()) {
                LOGGER.debug("Exceptions while updating: " + user);
                return "admin/user-update";
            }
            userService.updateUser(internalUser);

            // success
            page.setValid(true);
            page.setMessage("User updated successfully!");

            return "message";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

}
