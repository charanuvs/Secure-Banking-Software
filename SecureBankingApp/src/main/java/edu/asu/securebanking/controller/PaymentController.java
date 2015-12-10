package edu.asu.securebanking.controller;


import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.exceptions.AppBusinessException;
import edu.asu.securebanking.service.AccountService;
import edu.asu.securebanking.service.EmailService;
import edu.asu.securebanking.service.OTPService;
import edu.asu.securebanking.service.TransactionService;
import edu.asu.securebanking.util.TransactionValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Rishabh
 *         <p>
 *         Login controller
 */

@Controller
public class PaymentController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

    @Autowired
    private AccountService accountService;


    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionValidator transactionValidator;

    @RequestMapping(value = "/user/payments", method = RequestMethod.GET)
    public String payment(@ModelAttribute("transaction") Transaction transaction, final Model model) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);

        return "user/payment";
    }

    /**
     * @param transaction
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = {"/user/transfer",
            "/merch/transfer"}, method = RequestMethod.GET)
    public String transfer(@ModelAttribute("transaction") Transaction transaction, final Model model,
                           HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        String userType;

        if (AppConstants.ROLE_MERCHANT.equals(user.getUserType()))
            userType = "merch";
        else
            userType = "user";

        model.addAttribute("userType", userType);

        try {
            List<Account> accounts = accountService.getAccounts(user.getUserId());

            if (accounts == null || accounts.size() == 0) {
                page.setMessage("You do not have any accounts to make transfer");
                page.setValid(false);
                return "message";
            }
            // add accounts to the list
            model.addAttribute("accounts", accounts);
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

        return "user/transfer";
    }

    /**
     * @param transaction
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = "/user/payment", method = RequestMethod.GET)
    public String payment(@ModelAttribute("transaction") Transaction transaction, final Model model,
                          HttpSession session) {
        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);

        try {
            List<Account> accounts = accountService.getAccounts(user.getUserId());
            List<Account> merchantAccounts = accountService.getMerchantAccounts();

            if (accounts == null || accounts.size() == 0) {
                page.setMessage("You do not have any accounts to make transfer");
                page.setValid(false);
                return "message";
            }

            if (merchantAccounts == null || merchantAccounts.size() == 0) {
                page.setMessage("There are no merchant accounts to make a payment");
                page.setValid(false);
                return "message";
            }
            // add accounts to the list
            model.addAttribute("accounts", accounts);
            model.addAttribute("merchantAccounts", merchantAccounts);
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

        return "user/payment-new";
    }

    /**
     * @param transaction
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = "/user/payment", method = RequestMethod.POST)
    public String paymentPost(@ModelAttribute("transaction") Transaction transaction, final Model model,
                              HttpSession session,
                              BindingResult result) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);

        try {

            transaction.setTransactionType("PAYMENT");
            transaction.setStatus("PENDING");

            List<Account> accounts = accountService.getAccounts(user.getUserId());
            List<Account> merchantAccounts = accountService.getMerchantAccounts();

            if (accounts == null || accounts.size() == 0) {
                page.setMessage("You do not have any accounts to make transfer");
                page.setValid(false);
                return "message";
            }

            if (merchantAccounts == null || merchantAccounts.size() == 0) {
                page.setMessage("There are no merchant accounts to make a payment");
                page.setValid(false);
                return "message";
            }

            model.addAttribute("accounts", accounts);
            model.addAttribute("merchantAccounts", merchantAccounts);

            transactionValidator.validate(transaction, result);

            if (result.hasErrors())
                return "user/payment-new";

            if (transaction.getAmount().doubleValue() > AppConstants.TEN_K) {
                // critical
                session.setAttribute("transaction.critical.payment", transaction);

                // OTP and send the message
                String otp = otpService.generateOTP();
                session.setAttribute("transaction.critical.payment.otp", otp);
                // send email
                emailService.sendEmail(user.getEmail(), "OTP to submit your transaction",
                        "The OTP to submit your transaction: " + otp);

                return "user/submit-critical-transaction-payment";
            }

            transactionService.addNewTransaction(transaction, user.getUserId());
            // valid transaction
            // transactionService.addTransaction(transaction);
            page.setMessage("Payment Posted");
            page.setValid(true);
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setMessage(e.getMessage());
            page.setValid(false);
        } catch (Exception e) {
            LOGGER.error(e);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            page.setValid(false);

        }

        return "message";
    }


    /**
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = "/user/payment/confirm", method = RequestMethod.POST)
    public String paymentPostConfirmOTP(final Model model,
                                        HttpSession session,
                                        @RequestParam("otp") String otp) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        String sessionOtp = (String) session.getAttribute("transaction.critical.payment.otp");
        Transaction transaction = (Transaction) session.getAttribute("transaction.critical.payment");

        try {

            if (!StringUtils.hasText(otp) || transaction == null
                    || !sessionOtp.equals(otp)) {
                page.setMessage("Invalid request");
                page.setValid(false);
                return "message";
            }

            transactionService.addNewTransaction(transaction, user.getUserId());
            // valid transaction
            // transactionService.addTransaction(transaction);
            page.setMessage("Payment Posted");
            page.setValid(true);
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setMessage(e.getMessage());
            page.setValid(false);

        } catch (Exception e) {
            LOGGER.error(e);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            page.setValid(false);

        } finally {
            session.removeAttribute("transaction.critical.payment.otp");
            session.removeAttribute("transaction.critical.payment");
        }

        return "message";
    }

    /**
     * @param transaction
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = {"/user/transfer",
            "/merch/transfer"}, method = RequestMethod.POST)
    public String transferPost(@ModelAttribute("transaction") Transaction transaction, final Model model,
                               HttpSession session,
                               BindingResult result) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);

        String userType;
        if (AppConstants.ROLE_MERCHANT.equals(user.getUserType()))
            userType = "merch";
        else
            userType = "user";

        model.addAttribute("userType", userType);

        try {

            transaction.setTransactionType("TRANSFER");
            transaction.setStatus("PENDING");

            transactionValidator.validate(transaction, result);
            List<Account> accounts = accountService.getAccounts(user.getUserId());

            if (accounts == null || accounts.size() == 0) {
                page.setMessage("You do not have any accounts to make transfer");
                page.setValid(false);
                return "message";
            }
            // add accounts to the list
            model.addAttribute("accounts", accounts);

            if (result.hasErrors())
                return "user/transfer";

            if (transaction.getAmount().doubleValue() > AppConstants.TEN_K) {
                // critical
                session.setAttribute("transaction.critical", transaction);

                // OTP and send the message
                String otp = otpService.generateOTP();
                session.setAttribute("transaction.critical.otp", otp);
                // send email
                emailService.sendEmail(user.getEmail(), "OTP to submit your transaction",
                        "The OTP to submit your transaction: " + otp);

                return "user/submit-critical-transaction";
            }

            transactionService.addNewTransaction(transaction, user.getUserId());
            // valid transaction
            // transactionService.addTransaction(transaction);
            page.setMessage("Transfer Posted");
            page.setValid(true);
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setMessage(e.getMessage());
            page.setValid(false);

            return "message";
        } catch (Exception e) {
            LOGGER.error(e);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            page.setValid(false);

        }

        return "message";
    }

    /**
     * @param model
     * @param session
     * @return transfer
     */
    @RequestMapping(value = {"/user/transfer/confirm", "/merch/transfer/confirm"}, method = RequestMethod.POST)
    public String transferPostConfirmOTP(final Model model,
                                         HttpSession session,
                                         @RequestParam("otp") String otp) {

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("types", AppConstants.TRANSACTION_TYPES);
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        String sessionOtp = (String) session.getAttribute("transaction.critical.otp");
        Transaction transaction = (Transaction) session.getAttribute("transaction.critical");

        String userType;

        if (AppConstants.ROLE_MERCHANT.equals(user.getUserType()))
            userType = "merch";
        else
            userType = "user";

        model.addAttribute("userType", userType);

        try {

            if (!StringUtils.hasText(otp) || transaction == null
                    || !sessionOtp.equals(otp)) {
                page.setMessage("Invalid request");
                page.setValid(false);
                return "message";
            }

            transactionService.addNewTransaction(transaction, user.getUserId());
            // valid transaction
            // transactionService.addTransaction(transaction);
            page.setMessage("Transfer Posted");
            page.setValid(true);
        } catch (AppBusinessException e) {
            LOGGER.error(e);
            page.setMessage(e.getMessage());
            page.setValid(false);

        } catch (Exception e) {
            LOGGER.error(e);
            page.setMessage(AppConstants.DEFAULT_ERROR_MSG);
            page.setValid(false);

        } finally {
            session.removeAttribute("transaction.critical.otp");
            session.removeAttribute("transaction.critical");
        }

        return "message";
    }


    @RequestMapping(value = "/user/payment/validate",
            method = RequestMethod.POST)
    public String addTransaction(@ModelAttribute("transaction") Transaction transaction,
                                 Model model,
                                 BindingResult result,
                                 HttpSession session) throws AppBusinessException {

        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);

        // check fields
        if (!isNumeric(transaction.getToAccountNumber())
                || !isNumeric(transaction.getFromAccountNumber())
                || !isNumeric(transaction.getAmountString())
                || Double.parseDouble(transaction.getAmountString()) <= 0) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nAll fields must be positive numbers");
            return "/user/payment-deny";
        }

        LOGGER.info(transaction);

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        Integer toAccountNumberInt = Integer.parseInt(transaction.getToAccountNumber());
        Integer fromAccountNumberInt = Integer.parseInt(transaction.getFromAccountNumber());

        Account toAccount = accountService.getAccount(toAccountNumberInt);
        Account fromAccount = accountService.getAccount(fromAccountNumberInt);

        String transType = transaction.getTransactionType();
        BigDecimal amount = new BigDecimal(Double.parseDouble(transaction.getAmountString()), MathContext.DECIMAL64);


        // check if both accounts exist and are distinct
        if (toAccount == null || fromAccount == null || toAccountNumberInt == fromAccountNumberInt) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nPlease provide two valid and distinct account numbers.");
            return "/user/payment-deny";

        }
        // check if user owns the from account
        else if (!fromAccount.getUser().getUserId().equals(((AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER)).getUserId())) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nYou must be the owner of the source acount.");
            return "/user/payment-deny";
        }

        // Check if account types are appropriate for payment or transfer
        else if (!(transType.equals("PAYMENT")
                && toAccount.getAccountType().equals("MERCHANT")
                || transType.equals("TRANSFER")
                && !(toAccount.getAccountType().equals("MERCHANT")))) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nPayment transactions must be to a merchant acount, and transfers must be to user acounts.");
            return "/user/payment-deny";
        }
        // check for upper bound
        else if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nReason: Max amount for a single transaction is $1,000,000.");
            return "/user/payment-deny";
        }
        // check if user has sufficient funds
        else if (fromAccount.getBalance().compareTo(amount) == -1) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nReason: Insufficient funds.");
            return "/user/payment-deny";
        }
        // check for critical transaction
        else if (amount.compareTo(new BigDecimal("10000")) >= 0) {
            session.setAttribute("transaction.critical.amount", amount);
            session.setAttribute("transaction.critical.fromAccount", fromAccount);
            session.setAttribute("transaction.critical.toAccount", toAccount);
            session.setAttribute("transaction.critical.transactionType", transType);
            session.setAttribute("transaction.critical.transactionType", transType);


            session.setAttribute("transaction.critical.postUrl", "/user/payment/confirm-critical");

            // OTP and send the message
            String otp = otpService.generateOTP();
            session.setAttribute("transaction.critical.otp", otp);
            // send email
            emailService.sendEmail(loggedInUser.getEmail(), "OTP to submit your transaction",
                    "The OTP to submit your transaction: " + otp);

            return "user/submit-critical-transaction-otp";
        }
        // process the transaction
        else {

            transaction.setToAccount(toAccount);
            transaction.setFromAccount(fromAccount);

            transaction.setAmount(amount);
            transaction.setTransactionType(transType);
            transaction.setStatus(new String("PENDING"));
            //transaction.setAuthEmployee(userDAO.getUser(loggedInUser.getUserId()));

            Date currentDate = new Date();

            //Date Format needs to be like yyyy-mm-dd
            transaction.setDate(currentDate);

            session.setAttribute("user.payment", transaction);

            session.setAttribute("user.payment", transaction);
            LOGGER.info("Transnew: " + transaction);
            transactionService.addTransaction(transaction);


            return "/user/payment-confirm";
        }
    }


    @RequestMapping(value = "/user/payment/confirm-critical", method = RequestMethod.POST)
    public String addCriticalTransaction(/*@ModelAttribute("transaction") Transaction transaction,*/
                                         @RequestParam("otp") String otp,
                                         Model model,
                                         //BindingResult result,
                                         HttpSession session) throws AppBusinessException {

        LOGGER.info("OTP: " + otp);

        // validate OTP
        if (!otp.equals(session.getAttribute("transaction.critical.otp").toString())) {
            session.setAttribute("transaction.err", "Your transaction was not processed. \nReason: Incorrect OTP.");
            return "/user/payment-deny";
        }


        Transaction transaction = new Transaction();

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);
        model.addAttribute("page", page);


        Account toAccount = (Account) session.getAttribute("transaction.critical.toAccount");
        Account fromAccount = (Account) session.getAttribute("transaction.critical.fromAccount");

        String transType = (String) session.getAttribute("transaction.critical.transactionType");
        BigDecimal amount = (BigDecimal) session.getAttribute("transaction.critical.amount");

        //paymentValidator.validate(transaction, arg1);
        transaction.setToAccount(toAccount);
        transaction.setFromAccount(fromAccount);
        transaction.setAmount(amount);


        transaction.setTransactionType(transType);
        transaction.setStatus(new String("PENDING"));


        Date currentDate = new Date();

        //Date Format needs to be like yyyy-mm-dd
        transaction.setDate(currentDate);

        session.setAttribute("user.payment", transaction);

        AppUser loggedInUser = (AppUser)
                session.getAttribute(AppConstants.LOGGEDIN_USER);

        //transaction.setAuthEmployee(userDAO.getUser(loggedInUser.getUserId()));

        session.setAttribute("user.payment", transaction);
        LOGGER.info("Transnew: " + transaction);
        transactionService.addTransaction(transaction);


        return "/user/payment-confirm";

    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
