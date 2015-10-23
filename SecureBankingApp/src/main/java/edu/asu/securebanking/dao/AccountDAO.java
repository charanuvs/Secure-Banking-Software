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
    public List<Account> getAccounts(String username);

    /**
     * Add new account to the system
     *
     * @param account
     */
    public void addAccount(Account account);
}
