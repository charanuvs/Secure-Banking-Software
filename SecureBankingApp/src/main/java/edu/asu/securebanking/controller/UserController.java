package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Vikranth on 10/17/2015.
 */
@Controller
public class UserController {

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUserValidator;

    private static Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping(value = {"/user/home", "/user/", "/merch/home",
            "/merch/"}, method = RequestMethod.GET)
    public String home(HttpSession session,
                       Model model) {

        AppUser user = ((AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER));

        PageViewBean page = new PageViewBean();
        List<Account> accounts;

        model.addAttribute("accountTypes", AppConstants.ACCOUNT_TYPES);

        try {
            accounts = accountService.getAccounts(user.getUserId());
            LOGGER.info("Accounts size: " +
                    (null != accounts ? accounts.size() : "null"));
            model.addAttribute("accounts", accounts);
            model.addAttribute("username", user.getUserId());

            if (user.getUserType().equals(AppConstants.ROLE_MERCHANT))
                return "merch/account-list";
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setMessage(e.getMessage());
            page.setValid(false);

            return "message";
        } catch (Exception e) {
            LOGGER.error(e);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            page.setValid(false);

            return "message";
        }

        return "user/account-list";
    }


    @RequestMapping(value = "/user/payment", method = RequestMethod.GET)
    public String payment(@ModelAttribute("transaction")
                          Transaction transaction,
                          Model model) {
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        return "all/payment";
    }

    @RequestMapping(value = "/user/payment/confirm", method = RequestMethod.POST)
    public String payment(@ModelAttribute("transaction")
                          Transaction transaction,
                          BindingResult result,
                          Model model) {
        LOGGER.info(transaction);
        return "message";
    }
}
