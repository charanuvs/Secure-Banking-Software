package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.AccountService;
import edu.asu.securebanking.service.LoginService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.LoginFormValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Zahed
 *         <p>
 *         Account Summary controller
 */

@Controller
public class AccountSummaryController {

    private static final Logger LOGGER = Logger.getLogger(AccountSummaryController.class);

    @Autowired
    @Qualifier("loginFormValidator")
    LoginFormValidator loginFormValidator;

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    /**
     * @param session
     * @param model
     * @param accountId
     * @return
     */
    @RequestMapping(value = {"/user/statements/{accountId}",
            "/merch/statements/{accountId}"}, method = RequestMethod.GET)
    public String viewStatement(HttpSession session,
                                Model model,
                                @PathVariable("accountId") Integer accountId) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {

            AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
            boolean isAuth = accountService.isAuthorizedAccountHolder(user.getUserId(),
                    accountId);

            if (!isAuth)
                throw new AppBusinessException("Invalid account number");

            // get the account summary and transactions
            Account account = accountService.getAccount(accountId);

            // get the transactions
            List<Transaction> transactions = accountService.getTransactions(accountId);

            model.addAttribute("transactions", transactions);
            model.addAttribute("account", account);
            model.addAttribute("user", user.getUserId());

            if (user.getUserType().equals(AppConstants.ROLE_MERCHANT)) {
                return "merch/view-statements";
            }
        } catch (AppBusinessException e) {
            page.setValid(false);
            page.setMessage(e.getMessage());
            LOGGER.error(e);
            return "message";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            LOGGER.error(e);
            return "message";
        }

        return "user/view-statements";
    }
}
