package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.LoginFormBean;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.AccountService;
import edu.asu.securebanking.service.LoginService;
import edu.asu.securebanking.service.UserService;
import edu.asu.securebanking.util.LoginFormValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.servlet.http.HttpSession;

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
	 * @param login
	 * @return login-page
	 */

	@RequestMapping(value = { "/user/statements/{accountId}"}, method = RequestMethod.GET)
	public String viewStatement(HttpSession session, Model model, @PathVariable("accountId") Integer accountId) {

		PageViewBean page = new PageViewBean();
		model.addAttribute("page", page);

		try {
			AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
			List<Transaction> transactions = accountService.getAccountSummary(user.getUserId(),
					accountId);
			LOGGER.info("Transactions size: " + transactions.size());
			model.addAttribute("users", transactions);
			model.addAttribute("roles", AppConstants.INTERNAL_USERS_ROLES);
			model.addAttribute("status", AppConstants.USER_STATUS);
			return "user/view-statements";
		} catch (Exception e) {
			page.setValid(false);
			page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
			LOGGER.error(e);
			return "message";
		}
	}
}
