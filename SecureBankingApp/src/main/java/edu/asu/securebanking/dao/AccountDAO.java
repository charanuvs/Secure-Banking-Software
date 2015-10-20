package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;

import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
public interface AccountDAO {

    /**
     * Get accounts for a User
     *
     * @param user
     * @return accounts
     */
    public List<Account> getAccounts(AppUser user);
}
