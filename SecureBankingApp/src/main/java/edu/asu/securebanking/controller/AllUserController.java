package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.PasswordBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.EmailService;
import edu.asu.securebanking.service.OTPService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.AppUtil;
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

import javax.crypto.Cipher;
import javax.servlet.http.HttpSession;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUserValidator;

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
            session.setAttribute("password.user", pwd);
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
        PasswordBean pwd = (PasswordBean) session.getAttribute("password.user");
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);

        try {

            if (!StringUtils.hasText(sessionOtp)
                    || null == pwd) {
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
                userService.savePassword(loggedInUser.getUserId(),
                        pwd);
                page.setValid(true);
                page.setMessage("Password changed successfully!");
            }
        } catch (Exception e) {
            LOGGER.error(e);
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


    /**
     * Updates user info
     *
     * @param model
     * @param session
     * @return view
     */
    @RequestMapping(value = "/all/update",
            method = RequestMethod.GET)
    public String updateInfo(Model model,
                             HttpSession session) {

        if (!isUserPKIQualified(session)) {
            return "redirect:/all/key";
        }

        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("tranAuth", AppConstants.TRANSACTION_AUTHORIZED_YES);
        model.addAttribute("isExternalUser",
                AppConstants.EXTERNAL_USERS_ROLES.containsKey(loggedInUser.getUserType()));


        AppUser user;
        try {

            // Def get from database
            user = userService.getUser(loggedInUser.getUserId());
            session.setAttribute("updateUser", user);

            model.addAttribute("user", user);

            return "all/user-update-info";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    /**
     * User info update form
     *
     * @param model
     * @param user
     * @param session
     * @param result
     * @return view
     */
    @RequestMapping(value = "/all/update",
            method = RequestMethod.POST)
    public String updateInfo(Model model,
                             @ModelAttribute("user") AppUser user,
                             HttpSession session,
                             BindingResult result) {
        if (!isUserPKIQualified(session)) {
            return "redirect:/all/key";
        }

        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("tranAuth", AppConstants.TRANSACTION_AUTHORIZED_YES);
        model.addAttribute("isExternalUser",
                AppConstants.EXTERNAL_USERS_ROLES.containsKey(loggedInUser.getUserType()));

        LOGGER.info("Submitted user: " + user);
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
                return "all/user-update-info";
            }

            userService.updateUser(updateUser);
            // user has been updated
            // keep this user in the session
            session.setAttribute(AppConstants.LOGGEDIN_USER, updateUser);
            // No errors
            page.setValid(true);
            page.setMessage("Your information has been updated!");
            session.removeAttribute("PKI_Check_Passed");
            return "message";

        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    @RequestMapping(value = "all/key", method = RequestMethod.GET)
    public String key(Model model,
                      HttpSession session) {
        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("typeKey", "key");

        AppUser user;
        try {

            Base64.Decoder decoder = Base64.getDecoder();
            // Def get from database
            user = userService.getUser(loggedInUser.getUserId());
            session.setAttribute("updateUser", user);
            model.addAttribute("user", user);
            model.addAttribute("redr", "key");

            String secret = user.getUserId() + AppUtil.getRandomPwd(15);
            session.setAttribute("secret", secret);

            File pub = new File(AppConstants.KEY_PATH + user.getUserId() + "_pub.txt");
            FileInputStream pubfis = new FileInputStream(pub);
            DataInputStream pubdis = new DataInputStream(pubfis);
            byte[] pubBytes = new byte[(int) pub.length()];
            pubdis.readFully(pubBytes);
            pubdis.close();

            X509EncodedKeySpec pubspec =
                    new X509EncodedKeySpec(decoder.decode(pubBytes));
            KeyFactory pubkf = KeyFactory.getInstance("RSA");
            Key publickey = pubkf.generatePublic(pubspec);

            // Get an instance of the Cipher for RSA encryption/decryption
            Cipher c = Cipher.getInstance("RSA");
            // Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
            c.init(Cipher.ENCRYPT_MODE, publickey);

            byte[] encryptedBytes = c.doFinal(secret.getBytes());
            //System.out.println(new String(encryptedBytes));
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedencmessage = encoder.encode(encryptedBytes);
            String challengeString = new String(encodedencmessage);

            model.addAttribute("challengeString", challengeString);

            return "all/pki";

        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    @RequestMapping(value = "all/accountkey", method = RequestMethod.GET)
    public String keyForAccount(Model model,
                                HttpSession session) {
        AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("typeKey", "accountKey");

        AppUser user;
        try {

            Base64.Decoder decoder = Base64.getDecoder();
            // Def get from database
            user = userService.getUser(loggedInUser.getUserId());
            session.setAttribute("updateUser", user);
            model.addAttribute("user", user);
            model.addAttribute("redr", "key");

            String secret = user.getUserId() + AppUtil.getRandomPwd(15);
            session.setAttribute("secret", secret);

            File pub = new File(AppConstants.KEY_PATH + user.getUserId() + "_pub.txt");
            FileInputStream pubfis = new FileInputStream(pub);
            DataInputStream pubdis = new DataInputStream(pubfis);
            byte[] pubBytes = new byte[(int) pub.length()];
            pubdis.readFully(pubBytes);
            pubdis.close();

            X509EncodedKeySpec pubspec =
                    new X509EncodedKeySpec(decoder.decode(pubBytes));
            KeyFactory pubkf = KeyFactory.getInstance("RSA");
            Key publickey = pubkf.generatePublic(pubspec);

            // Get an instance of the Cipher for RSA encryption/decryption
            Cipher c = Cipher.getInstance("RSA");
            // Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
            c.init(Cipher.ENCRYPT_MODE, publickey);

            byte[] encryptedBytes = c.doFinal(secret.getBytes());
            //System.out.println(new String(encryptedBytes));
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedencmessage = encoder.encode(encryptedBytes);
            String challengeString = new String(encodedencmessage);

            model.addAttribute("challengeString", challengeString);

            return "all/pki";

        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    @RequestMapping(value = "all/accountKey", method = RequestMethod.POST)
    public String handleAccountKey(HttpSession session,
                                   @RequestParam("secret") String submittedSecret,
                                   Model model) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        try {
            AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
            String challengeString = AppUtil.getRandomPwd(15);

            //----------------------Encrypting and Encoding using uploaded key------------------

            if (submittedSecret.equals((String) session.getAttribute("secret"))) {
                session.setAttribute("PKI_Account_Dwnld_Check_Passed", "true");
            } else {
                session.setAttribute("PKI_Account_Dwnld_Check_Passed", "false");
                page.setValid(false);
                page.setMessage("Secret code does not match.");
                return "message";
            }

            return "redirect:/";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    @RequestMapping(value = "all/key", method = RequestMethod.POST)
    public String handleKey(HttpSession session,
                            @RequestParam("secret") String submittedSecret,
                            Model model) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        try {
            AppUser loggedInUser = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
            String challengeString = AppUtil.getRandomPwd(15);

            //----------------------Encrypting and Encoding using uploaded key------------------

            if (submittedSecret.equals((String) session.getAttribute("secret"))) {
                session.setAttribute("PKI_Check_Passed", "true");
            } else {
                session.setAttribute("PKI_Check_Passed", "false");
                page.setValid(false);
                page.setMessage("Secret code does not match.");
                return "message";
            }

            return "redirect:/all/update";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }
    }

    /**
     * @return view
     */
    @RequestMapping(value = "/pwd", method = RequestMethod.GET)
    private String forgotPassword() {
        return "all/forgot-pwd";
    }

    /**
     * @return view
     */
    @RequestMapping(value = "/pwd", method = RequestMethod.POST)
    private String forgotPassword(@RequestParam("username")
                                  String username,
                                  Model model,
                                  HttpSession session) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {

            if (!StringUtils.hasText(username)) {
                page.setValid(false);
                page.setMessage("Invalid request");
                return "message";
            }

            // user not logged in
            AppUser user = userService.getUser(username);
            if (user == null) {
                page.setValid(false);
                page.setMessage("Invalid request");
                return "message";
            }

            // Send the OTP
            String otp = otpService.generateOTP();
            emailService.sendEmail(user.getEmail(),
                    "OTP to change the password",
                    "Your OTP to change the password: " + otp);

            session.setAttribute("user.password.forgot.otp", otp);
            model.addAttribute("username", username);
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e.getMessage());

            return "message";
        }

        return "all/forgot-pwd-otp";
    }

    /**
     * @return view
     */
    @RequestMapping(value = "/pwd/reset", method = RequestMethod.POST)
    private String resetPassword(@RequestParam("username")
                                 String username,
                                 @RequestParam("otp")
                                 String otp,
                                 Model model,
                                 HttpSession session) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        String sessionOtp = (String)
                session.getAttribute("user.password.forgot.otp");

        try {
            LOGGER.info(sessionOtp);
            if (!StringUtils.hasText(username)
                    || !StringUtils.hasText(otp)
                    || !StringUtils.hasText(sessionOtp)) {
                page.setValid(false);
                page.setMessage("Invalid request");
            } else {

                // user not logged in
                AppUser user = userService.getUser(username);
                if (user == null) {
                    page.setValid(false);
                    page.setMessage("Invalid request");
                } else if (!sessionOtp.equals(otp)) {
                    page.setValid(false);
                    page.setMessage("Invalid OTP. Please try again");
                }

                // Save the password
                // Set the defaults
                String pwd = AppUtil.getRandomPwd();

                emailService.sendEmail(user.getEmail(),
                        "Password reset", "Your password has been reset: " + pwd);

                userService.savePassword(username, pwd);

                page.setMessage("Your password has been reset. " +
                        "Please check your email for the new password.");
                page.setValid(true);
            }
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e);

        } finally {
            session.removeAttribute("user.password.forgot.otp");
        }

        return "message";
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
        dbUser.setTransAuth(reqUser.getTransAuth());
    }

    /**
     * @param session
     * @return bool
     */
    private boolean isUserPKIQualified(HttpSession session) {
        String PKIFlag = (String) session.getAttribute("PKI_Check_Passed");
        return StringUtils.hasText(PKIFlag) && PKIFlag.equals("true");

    }
}
