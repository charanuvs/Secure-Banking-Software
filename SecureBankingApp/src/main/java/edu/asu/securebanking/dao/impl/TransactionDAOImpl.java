package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.TransactionDAO;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rishabh
 * @author Andrew
 */

public class TransactionDAOImpl extends AbstractDAO implements TransactionDAO {

    private static Logger LOGGER = Logger.getLogger(TransactionDAOImpl.class);

    @Override
    public void updateTransaction(Transaction t) {
        getSession().update(t);
    }

    @Override
    public void deleteTransaction(Transaction t) {
        getSession().delete(t);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        getSession().save(transaction);
    }

    @Override
    public Transaction getTransaction(String transactionId) {
        Integer intTransactionId = Integer.parseInt(transactionId);
        return (Transaction) getSession().get(Transaction.class, intTransactionId);
    }

    @Override
    public List<Transaction> getPendingCriticalTransactions() {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("status", "PENDING"));
        criteria.addOrder(Order.asc("date"));
        return criteria.list();
    }

    @Override
    public List<Transaction> getPendingNonCriticalTransactions() {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("status", "PENDING"));
        criteria.add(Restrictions.lt("amount", new BigDecimal("10000")));
        criteria.addOrder(Order.asc("date"));
        return criteria.list();
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
