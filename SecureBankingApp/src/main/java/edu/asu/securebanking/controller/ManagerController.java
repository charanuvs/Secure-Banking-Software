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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
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

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            List<AppUser> users = userService.getExternalUsers();

            LOGGER.info("Users size: " + users.size());
            model.addAttribute("users", users);
            model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);
            model.addAttribute("status", AppConstants.USER_STATUS);
            return "manage/user-list";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "manage/message";
        }
    }

    /**
     * Update user form
     *
     * @param username
     * @param model
     * @return
     */
    @RequestMapping(value = "/manage/user/update/{id}",
            method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") String username,
                             Model model) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        // Get user
        try {
            AppUser user = userService.getExternalUser(username);

            if (null == user) {
                page.setValid(false);
                page.setMessage("The user does not exist");
                return "manage/message";
            }

            // User exists
            model.addAttribute("user", user);
            // other attributes
            model.addAttribute("genders", AppConstants.GENDERS);
            model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);
            model.addAttribute("status", AppConstants.USER_STATUS);
            return "manage/user-update";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "manage/message";
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
    @RequestMapping(value = "/manage/user/update",
            method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") AppUser user,
                             Model model,
                             BindingResult result) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);
        model.addAttribute("status", AppConstants.USER_STATUS);

        // Everything is good
        try {

            LOGGER.info("Update user: " + user);

            if (user.getUserId() == null || user.getUserId().trim().length() == 0) {
                page.setValid(false);
                page.setMessage("Invalid user update");

                return "manage/message";
            }

            // Get the user with the username
            AppUser externalUser = null;
            try {
                externalUser = userService.getExternalUser(user.getUserId());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

            if (null == externalUser) { // not an external user
                page.setValid(false);
                page.setMessage("Invalid user update");

                return "manage/message";
            }

            // Copy the values to be updated
            copySubUserToDBUser(user, externalUser);

            // Validate the inputs
            externalUserValidator.validate(externalUser, result);

            if (result.hasErrors()) {
                LOGGER.debug("Exceptions while updating: " + user);
                return "manage/user-update";
            }
            userService.updateUser(externalUser);

            // success
            page.setValid(true);
            page.setMessage("User updated successfully!");

            return "manage/message";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "manage/message";
        }
    }

    @RequestMapping(value = "/manage/update",
            method = RequestMethod.GET)
    public String updateInfo(Model model,
                             HttpSession session) {

        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        AppUser user;
        try {

            // Def get from database
            user = userService.getUser(loggedInUser.getUserId());
            session.setAttribute("updateUser", user);

            model.addAttribute("user", user);

            return "manage/user-update-info";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "manage/message";
        }
    }

    @RequestMapping(value = "/manage/update",
            method = RequestMethod.POST)
    public String updateInfo(Model model,
                             @ModelAttribute("user") AppUser user,
                             HttpSession session,
                             BindingResult result) {

        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        // From session
        AppUser updateUser = (AppUser) session.getAttribute("updateUser");

        try {
            if (updateUser == null) {
                updateUser = userService.getUser(loggedInUser.getUserId());
                session.setAttribute("updateUser", updateUser);
            }

            // Copy necessary attributes to user
            copyUserInfo(updateUser, user);

            appUserValidator.validate(updateUser, result);

            if (result.hasErrors()) {
                return "manage/user-update-info";
            }

            userService.updateUser(updateUser);
            // user has been updated
            // keep this user in the session
            session.setAttribute(AppConstants.LOGGEDIN_USER, updateUser);
            // No errors
            page.setValid(true);
            page.setMessage("Your information has been updated!");
            return "manage/message";

        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "manage/message";
        }
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

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        model.addAttribute("genders", AppConstants.GENDERS);
        model.addAttribute("roles", AppConstants.EXTERNAL_USERS_ROLES);

        try {

            // Set the defaults
            user.setTempPassword(AppUtil.getRandomPwd());
            user.setPassword(user.getTempPassword());
            user.setConfirmPassword(user.getConfirmPassword());
            user.setStatus(AppConstants.USER_ACTIVE);
            // Validate all the details

            LOGGER.info("User: " + user);

            externalUserValidator.validate(user, result);

            if (result.hasErrors()) {
                LOGGER.debug("Errors in appuser obj");
                return "manage/user-add";
            }

            AppUser prevUser = null;

            prevUser = userService.getUser(user.getUserId());


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

            page.setValid(true);
            page.setMessage("User created with username '" + user.getUserId() + "'");
            model.addAttribute("page", page);

            return "manage/message";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "manage/message";
        }

    }

    private static void copySubUserToDBUser(AppUser reqUser, AppUser dbUser) {
        if (reqUser == null || dbUser == null)
            return;
        // end
        dbUser.setName(reqUser.getName());
        dbUser.setEmail(reqUser.getEmail());
        dbUser.setAddress(reqUser.getAddress());
        dbUser.setPhoneNumber(reqUser.getPhoneNumber());
        dbUser.setStatus(reqUser.getStatus());
        dbUser.setGender(reqUser.getGender());
        dbUser.setDateString(reqUser.getDateString());
        dbUser.setSsn(reqUser.getSsn());
        // end
    }

    /**
     * Copy only update info fields
     *
     * @param dbUser
     * @param reqUser
     */
    private static void copyUserInfo(AppUser dbUser, AppUser reqUser) {
        dbUser.setName(reqUser.getName());
        dbUser.setEmail(reqUser.getEmail());
        dbUser.setAddress(reqUser.getAddress());
        dbUser.setPhoneNumber(reqUser.getPhoneNumber());
    }
}
