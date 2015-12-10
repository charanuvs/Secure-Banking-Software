package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PII;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.PIIService;
import edu.asu.securebanking.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Vikranth on 10/25/2015.
 */
@Controller
public class GovController {

    @Autowired
    private UserService userService;

    @Autowired
    private PIIService piiService;

    private static Logger LOGGER = Logger.getLogger(GovController.class);

    /**
     * Get all internal users for Gov agaency
     *
     * @param model
     * @return internalUsers
     */
    @RequestMapping(value = {"/gov/users", "/gov"},
            method = RequestMethod.GET)
    public String home(Model model) {

        List<AppUser> users = userService.getInternalUsers();
        model.addAttribute("users", users);

        return "gov/user-list";
    }

    /**
     * @param model
     * @return piiRequests
     */
    @RequestMapping(value = "/gov/requests", method = RequestMethod.GET)
    public String getRequests(Model model) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            List<PII> piiRequests = piiService.getPIIRequests();
            model.addAttribute("piiRequests", piiRequests);

        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(e.getMessage());

            return "message";
        } catch (Exception e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }

        return "gov/pii-request-list";
    }

    /**
     * PII request form
     *
     * @param model
     * @param userId
     * @param toUserId
     * @return view
     */
    @RequestMapping(value = "/gov/authorize/{userId}/{toUserId}",
            method = RequestMethod.GET)
    public String confirmAuthorizeRequest(Model model,
                                          @PathVariable("userId")
                                          String userId,
                                          @PathVariable("toUserId")
                                          String toUserId) {

        model.addAttribute("userId", userId);
        model.addAttribute("toUserId", toUserId);

        return "gov/pii-request-form";
    }

    /**
     * PII request details form
     *
     * @param model
     * @param userId
     * @return view
     */
    @RequestMapping(value = "/gov/request/{userId}",
            method = RequestMethod.GET)
    public String requestUserDetails(Model model,
                                     @PathVariable("userId")
                                     String userId) {

        model.addAttribute("userId", userId);

        return "gov/pii-request-details-form";
    }

    /**
     * Authorize the request
     *
     * @param model
     * @param userId
     * @param toUserId
     * @return view
     */
    @RequestMapping(value = "/gov/authorize/", method = RequestMethod.POST)
    public String authorizeRequest(Model model,
                                   String userId,
                                   String toUserId) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        LOGGER.info("userId: " + userId + ", toUserId: " + toUserId);
        if (!StringUtils.hasText(userId) ||
                !StringUtils.hasText(toUserId)) {
            page.setMessage("Invalid request");
            page.setValid(false);

            return "message";
        }

        try {
            piiService.authorizeRequest(userId, toUserId);

            page.setValid(true);
            page.setMessage("Authorization successful");
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(e.getMessage());

        } catch (Exception e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
        }

        return "message";
    }

    /**
     * Authorize the request
     *
     * @param model
     * @param userId
     * @return view
     */
    @RequestMapping(value = "/gov/request/", method = RequestMethod.POST)
    public String addRequestUserDetails(Model model,
                                        String userId) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        LOGGER.info("userId: " + userId);
        if (!StringUtils.hasText(userId)) {
            page.setMessage("Invalid request");
            page.setValid(false);
            return "message";
        }

        try {
            piiService.requestUserDetails(userId);
            page.setValid(true);
            page.setMessage("Request successful");
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(e.getMessage());

        } catch (Exception e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
        }

        return "message";
    }
}
