package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    @Transactional
    public List<Account> getAccounts(AppUser user) {
        return accountDAO.getAccounts(user);
    }
}
