package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.AccountDAO;

import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class AccountDAOImpl extends AbstractDAO implements AccountDAO {
    @Override
    public List<Account> getAccounts(AppUser user) {
        return null;
    }
}
