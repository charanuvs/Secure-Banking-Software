package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.PasswordBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.EmailService;
import edu.asu.securebanking.service.OTPService;
import edu.asu.securebanking.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by Vikranth on 10/20/2015.
 */
@Controller
public class AllUserController {

    @Autowired
    @Qualifier("passwordValidator")
    private Validator passwordValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    private static Logger LOGGER = Logger.getLogger(AllUserController.class);

    /**
     * Change password
     *
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/all/pwd",
            method = RequestMethod.GET)
    public String updatePassword(@ModelAttribute("pwd")
                                 PasswordBean pwd) {
        return "all/update-pwd";
    }

    /**
     * @param model
     * @param session
     * @param result
     * @return view
     */
    @RequestMapping(value = {"/all/pwd"},
            method = RequestMethod.POST)
    public String updatePassword(
            @ModelAttribute("pwd") PasswordBean pwd,
            Model model,
            HttpSession session,
            BindingResult result) {
        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);
        String role = loggedInUser.getUserType();

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            passwordValidator.validate(pwd, result);

            if (result.hasErrors()) { // errors
                // empty
                pwd.setConfirmPassword("");
                pwd.setCurrentPassword("");
                pwd.setPassword("");

                return "all/update-pwd";
            }

            // No errors in validator
            AppUser user = userService.getUserforPwd(loggedInUser.getUserId(), pwd);
            // Add this user to session
            session.setAttribute("password.user", user);
            // OTP and send the message
            String otp = otpService.generateOTP();
            session.setAttribute("password.user.otp", otp);
            // send email
            emailService.sendEmail(user.getEmail(), "OTP to change your password",
                    "The OTP to change your password: " + otp);

        } catch (AppBusinessException e) {
            page.setValid(false);
            page.setMessage(e.getMessage());

            return "message";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }

        return "all/update-pwd-otp";
    }

    /**
     * OTP for the password
     *
     * @param otp
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/all/pwd/otp", method = RequestMethod.POST)
    public String updatePasswordWithOTP(@RequestParam("otp") String otp,
                                        HttpSession session,
                                        Model model) {

        // if password.user and password.user.otp does not exist
        // return
        String sessionOtp = (String) session.getAttribute("password.user.otp");
        AppUser user = (AppUser) session.getAttribute("password.user");
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {

            if (!StringUtils.hasText(sessionOtp)
                    || null == user) {
                LOGGER.debug("session OTP is empty: " + sessionOtp);
                // remove session variables and redirect
                page.setMessage("Invalid request");
                page.setValid(false);

            } else if (!sessionOtp.equals(otp)) {
                // remove session variables and redirect
                page.setMessage("Invalid OTP");
                page.setValid(false);

            } else {
                // Update user with the new password
                userService.updateUser(user);

                page.setValid(true);
                page.setMessage("Password changed successfully!");
            }
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
        } finally {
            // remove session attributes password.user
            // and password.user.otp
            session.removeAttribute("password.user.otp");
            session.removeAttribute("password.user");
        }

        return "message";
    }


}
