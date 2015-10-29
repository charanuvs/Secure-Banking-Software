package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.AccountDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class AccountDAOImpl extends AbstractDAO implements AccountDAO {

    @Override
    public List<Account> getAccounts(String username) {

        Criteria criteria = getSession().createCriteria(Account.class);
        criteria.add(Restrictions.eq("user.userId", username));

        return criteria.list();
    }

    @Override
    public List<Account> getMerchantAccounts() {

        Criteria criteria = getSession().createCriteria(Account.class);

        return criteria.list();
    }

    @Override
    public void addAccount(Account account) {
        getSession().save(account);
    }

    @Override
    public Account getAccount(Integer accountNum) {
        return (Account) getSession().get(Account.class, accountNum);
    }

    @Override
    public void updateAccount(Account account) {
        getSession().update(account);
    }
}