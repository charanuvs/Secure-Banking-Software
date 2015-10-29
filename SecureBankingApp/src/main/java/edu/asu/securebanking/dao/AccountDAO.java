package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Account;

import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
public interface AccountDAO {

    /**
     * Get accounts for a User
     *
     * @param username
     * @return accounts
     */
    List<Account> getAccounts(String username);

    /**
     * Add new account to the system
     *
     * @param account
     */
    void addAccount(Account account);

    /**
     * Get account
     *
     * @param accountNum
     */
    Account getAccount(Integer accountNum);

    /**
     * @param account
     */
    void updateAccount(Account account);

    /**
     * @return accounts
     */
    List<Account> getMerchantAccounts();
}
