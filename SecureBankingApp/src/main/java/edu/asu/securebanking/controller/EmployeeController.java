package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.AccountService;
import edu.asu.securebanking.service.EmailService;
import edu.asu.securebanking.service.TransactionService;
import edu.asu.securebanking.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Created by Vikranth on 10/18/2015.
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    @Qualifier("externalUserValidator")
    private Validator externalUserValidator;

    @Autowired
    @Qualifier("appUserValidator")
    private Validator appUserValidator;

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("accountValidator")
    private Validator accountValidator;

    private static Logger LOGGER = Logger.getLogger(ManagerController.class);

    /**
     * Manager home page
     *
     * @return home
     */
    @RequestMapping(value = {"/emp/home"},
            method = RequestMethod.GET)

    public String home() {
        return "emp/home";
    }


    // ADDED 10/27/2015 -- ANDREW

    /**
     * list all non critical transactions
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/emp/transactions/list", "/emp/"}, method = RequestMethod.GET)
    public String getNonCriticalTransactions(Model model) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            List<Transaction> transactions = transactionService.getPendingNonCriticalTransactions();

            LOGGER.info("Transactions size: " + transactions.size());
            model.addAttribute("transactions", transactions);
            return "/emp/transaction-list";
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
    @RequestMapping(value = "/emp/transactions/approve/{transactionId}",
            method = RequestMethod.GET)
    public String approveTransaction(@PathVariable("transactionId") String transactionId,
                                     Model model,
                                     HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        try {
            Transaction transaction = transactionService.getTransaction(transactionId);

            // check if the transaction has been approved or not
            if (transaction.getStatus().equals("APPROVED")) {
                session.setAttribute("approval.err", "The transaction you requested has already been approved");
                return "/emp/transaction-approval-error";
            }
            // check if the user has sufficient funds
            if (transaction.getAmount().compareTo(transaction.getFromAccount().getBalance()) > 0) {
                session.setAttribute("approval.err", "Cannot approve due to insufficient funds");
                return "/emp/transaction-approval-error";
            }

            transaction.setStatus("COMPLETE");

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
            return "redirect:/emp/transactions/list";
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
    @RequestMapping(value = "/emp/transactions/deny/{transactionId}",
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
                session.setAttribute("approval.err", "The transaction you requested has already been approved");
                return "/emp/transactions/approval-error";
            }

            LOGGER.info("TRANSACTION: " + transaction);

            transactionService.deleteTransaction(transaction);

            // return updated transaction list
            return "redirect:/emp/transactions/list";
        } catch (Exception e) {
            page.setValid(false);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);

            return "message";
        }
    }

}
