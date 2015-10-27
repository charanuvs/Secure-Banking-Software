package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.TransactionDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class TransactionDAOImpl extends AbstractDAO implements TransactionDAO {

    @Override
    public void addTransaction(Transaction transaction) {
        getSession().save(transaction);
    }

    @Override
    public List<Transaction> getTransactions(Integer accountNum) {

        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.or(
                Restrictions.eq("toAccount.accountNum", accountNum),
                Restrictions.eq("fromAccount.accountNum", accountNum)
        ));
        criteria.addOrder(Order.desc("date"));

        return criteria.list();
    }
}
