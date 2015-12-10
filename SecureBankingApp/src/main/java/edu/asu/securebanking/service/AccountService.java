package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.AccountDAO;
import edu.asu.securebanking.dao.TransactionDAO;
import edu.asu.securebanking.dao.UserDAO;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    @Qualifier("userDAO")
    private UserDAO userDAO;

    @Autowired
    @Qualifier("transactionDAO")
    private TransactionDAO transactionDAO;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    /**
     * List accounts of a user
     *
     * @param username
     * @return accounts
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<Account> getAccounts(String username) throws AppBusinessException {

        // Get the user
        AppUser user = userDAO.getUser(username);

        if (user == null ||
                !AppConstants.EXTERNAL_USERS_ROLES.containsKey(user.getUserType())) {
            // User is not internal user. Throw an exception
            throw new AppBusinessException(messageSource.getMessage("account.notexternaluser.error",
                    new Object[]{}, Locale.getDefault()));
        }

        return accountDAO.getAccounts(username);
    }

    /**
     * List accounts of a user
     *
     * @param username
     * @return accounts
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<Account> getAuthAccounts(String username) throws AppBusinessException {

        // Get the user
        AppUser user = userDAO.getUser(username);

        if (user == null ||
                !AppConstants.EXTERNAL_USERS_ROLES.containsKey(user.getUserType())
                || AppConstants.TRANSACTION_AUTHORIZED_NO.equals(user.getTransAuth())) {
            // User is not internal user. Throw an exception
            throw new AppBusinessException(messageSource.getMessage("account.notexternaluser.error",
                    new Object[]{}, Locale.getDefault()));
        }

        return accountDAO.getAccounts(username);
    }

    /**
     * Add new account to the system
     *
     * @param username
     * @param account
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public void addAccount(String username, Account account)
            throws AppBusinessException {
        AppUser user = userDAO.getUser(username);

        if (null == user) {
            throw new AppBusinessException("User does not exist");
        }

        // Check if account type is valid
        String userType = user.getUserType();

        // For merchant
        if (AppConstants.ROLE_MERCHANT.equals(userType)) {
            account.setAccountType(AppConstants.ACCOUNT_MERCHANT);
        } else if (!AppConstants.ACCOUNT_TYPES_NORMAL
                .containsKey(account.getAccountType())) { // For customer
            // Invalid account type
            throw new AppBusinessException(messageSource.getMessage("account.type.error",
                    new Object[]{}, Locale.getDefault()));
        }

        // everythings good - set the defaults
        account.setUser(user);
        account.setOpeningDate(new Date());

        accountDAO.addAccount(account);
    }

    /**
     * @param accountNum
     * @return
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<Transaction> getTransactions(Integer accountNum) {
        return transactionDAO.getTransactions(accountNum);
    }

    /**
     * @param username
     * @param accountNum
     * @return isAuth
     */
    @Transactional
    public boolean isAuthorizedAccountHolder(String username,
                                             Integer accountNum) {
        boolean isAuth = false;
        Account account = accountDAO.getAccount(accountNum);

        if (account == null ||
                !account.getUser().getUserId().equals(username)) {
            return false;
        }

        return true;
    }

    /**
     * Get account from account num
     *
     * @param accountNum
     * @return account
     */
    @Transactional
    public Account getAccount(Integer accountNum) {
        return accountDAO.getAccount(accountNum);
    }

    /**
     * @param account
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public void updateAccount(Account account)
            throws AppBusinessException {
        accountDAO.updateAccount(account);
    }

    /**
     * @return merchantAccounts
     * @throws AppBusinessException
     */
    @Transactional
    public List<Account> getMerchantAccounts() throws AppBusinessException {
        List<Account> accounts = accountDAO.getMerchantAccounts();

        Iterator<Account> it = accounts.iterator();

        while (it.hasNext()) {
            Account account = it.next();
            if (!account.getUser().getUserType().equals(AppConstants.ROLE_MERCHANT)) {
                it.remove();
            }
        }

        return accounts;
    }
}
