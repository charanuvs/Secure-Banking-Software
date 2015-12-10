package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.AccountService;
import edu.asu.securebanking.service.EmailService;
import edu.asu.securebanking.service.TransactionService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


/**
 * Created by Vikranth on 10/18/2015.
 */
@Controller
public class ManagerController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("externalUserValidator")
    private Validator externalUserValidator;

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("accountValidator")
    private Validator accountValidator;

    @Autowired
    private TransactionService transactionService;

    private static Logger LOGGER = Logger.getLogger(ManagerController.class);


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
        model.addAttribute("tranAuth", AppConstants.TRANSACTION_AUTHORIZED_YES);

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
                return "message";
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

                return "message";
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

                return "message";
            }

            // Copy the values to be updated
            AppUtil.copySubUserToDBUser(user, externalUser);

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

            return "message";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

    /**
     * Get accounts for external user
     *
     * @param username
     * @param model
     * @return view
     */
    @RequestMapping(value = "/manage/account/{id}",
            method = RequestMethod.GET)
    public String getAccounts(@PathVariable("id") String
                                      username,
                              Model model) {
        PageViewBean page = new PageViewBean();
        List<Account> accounts;
        model.addAttribute("page", page);

        model.addAttribute("accountTypes", AppConstants.ACCOUNT_TYPES);

        try {
            accounts = accountService.getAccounts(username);
            LOGGER.info("Accounts size: " +
                    (null != accounts ? accounts.size() : "null"));
            model.addAttribute("accounts", accounts);
            model.addAttribute("username", username);
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

        return "manage/account-list";
    }

    /**
     * Add new account form
     *
     * @param username
     * @param account
     * @param model
     * @param session
     * @return view
     */
    @RequestMapping(value = "/manage/account/add/{id}",
            method = RequestMethod.GET)
    public String addAccount(@PathVariable("id") String username,
                             @ModelAttribute("account") Account account,
                             Model model,
                             HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            if (!StringUtils.hasText(username)) {
                page.setMessage("Username required");
                page.setValid(false);

                return "message";
            }

            AppUser user = userService.getUser(username);
            if (user == null ||
                    !AppConstants.EXTERNAL_USERS_ROLES.containsKey(
                            user.getUserType())) {
                page.setMessage("Invalid external user");
                page.setValid(false);

                return "message";
            }

            session.setAttribute("user.account.add.userId", user.getUserId());
            Map<String, String> accountTypes;
            if (!AppConstants.ROLE_MERCHANT.equals(user.getUserType()))
                accountTypes = AppConstants.ACCOUNT_TYPES_NORMAL;
            else
                accountTypes = AppConstants.ACCOUNT_TYPES_MERCHANT;

            model.addAttribute("accountTypes", accountTypes);
            session.setAttribute("user.account.add.types", accountTypes);

            // return account form
            return "manage/account-add";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

    @RequestMapping(value = "/manage/account/add",
            method = RequestMethod.POST)
    public String addAccount(@ModelAttribute("account")
                             Account account,
                             Model model,
                             BindingResult result,
                             HttpSession session) {

        String username = (String)
                session.getAttribute("user.account.add.userId");
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        Map<String, String> accountTypes = (Map<String, String>)
                session.getAttribute("user.account.add.types");

        try {

            accountValidator.validate(account, result);

            if (!StringUtils.hasText(username)
                    || null == accountTypes) {
                page.setValid(false);
                page.setMessage("Invalid request");
            } else if (result.hasErrors()) {
                model.addAttribute("accountTypes", accountTypes);
                return "manage/account-add";
            } else {
                accountService.addAccount(username, account);
                page.setMessage("Account has been created");
                session.removeAttribute("user.account.add.userId");
                session.removeAttribute("user.account.add.types");
            }
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(e.getMessage());
            session.removeAttribute("user.account.add.userId");
            session.removeAttribute("user.account.add.types");
        } catch (Exception e) {
            LOGGER.error(e);
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            session.removeAttribute("user.account.add.userId");
            session.removeAttribute("user.account.add.types");
        }

        return "message";
    }

    /**
     * Add a external user
     *
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
        model.addAttribute("tranAuth", AppConstants.TRANSACTION_AUTHORIZED_YES);

        try {

            // Set the defaults
            user.setTempPassword(AppUtil.getRandomPwd());
            user.setPassword(user.getTempPassword());
            user.setConfirmPassword(user.getConfirmPassword());
            user.setStatus(AppConstants.USER_ACTIVE);
            // Default is no
            user.setTransAuth(AppConstants.TRANSACTION_AUTHORIZED_NO);
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

    // ADDED 10/27/2015 -- ANDREW

    /**
     * list all critical transactions
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/manage/transactions/list",
            "/manage/", "/manage/home"}, method = RequestMethod.GET)
    public String getCriticalTransactions(Model model) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            List<Transaction> transactions = transactionService.getPendingCriticalTransactions();

            LOGGER.info("Transactions size: " + transactions.size());
            model.addAttribute("transactions", transactions);
            return "/manage/transaction-list";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

    // ADDED 10/28/2015 -- ANDREW

    /**
     * Approve a transaction
     *
     * @param model
     */
    @RequestMapping(value = "/manage/transactions/approve/{transactionId}",
            method = RequestMethod.GET)
    public String approveTransaction(@PathVariable("transactionId") String transactionId,
                                     Model model,
                                     HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);

        try {
            Transaction transaction = transactionService.getTransaction(transactionId);

            // check if the transaction has been approved or not
            if (transaction.getStatus().equals("APPROVED")) {
                page.setMessage("The transaction you requested has already been approved");
                page.setValid(false);
                return "message";
            }
            // check if the user has sufficient funds
            if (transaction.getAmount().compareTo(transaction.getFromAccount().getBalance()) > 0) {
                page.setValid(false);
                page.setMessage("Cannot approve due to insufficient funds");
                return "message";
            }

            transaction.setStatus("COMPLETE");
            transaction.setAuthEmployee(loggedInUser);

            LOGGER.info("TRANSACTION: " + transaction);

            // TODO
            //transaction.setAuthEmployee(authEmployee);

            Account toAccount = accountService.getAccount(transaction.getToAccount().getAccountNum());
            Account fromAccount = accountService.getAccount(transaction.getFromAccount().getAccountNum());
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));

            transactionService.updateTransaction(transaction);
            accountService.updateAccount(toAccount);
            accountService.updateAccount(fromAccount);

            // return updated transaction list
            return "redirect:/manage/";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }


    // ADDED 10/28/2015 -- ANDREW

    /**
     * Approve a transaction
     *
     * @param model
     */
    @RequestMapping(value = "/manage/transactions/deny/{transactionId}",
            method = RequestMethod.GET)
    public String denyTransaction(@PathVariable("transactionId") String transactionId,
                                  Model model,
                                  HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            Transaction transaction = transactionService.getTransaction(transactionId);

            // check if the transaction has been approved or not
            if (transaction.getStatus().equals("APPROVED")) {
                page.setMessage("The transaction you requested has already been approved");
                page.setValid(false);
                return "message";
            }

            LOGGER.info("TRANSACTION: " + transaction);

            transactionService.deleteTransaction(transaction);

            // return updated transaction list
            return "redirect:/manage/";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

}
