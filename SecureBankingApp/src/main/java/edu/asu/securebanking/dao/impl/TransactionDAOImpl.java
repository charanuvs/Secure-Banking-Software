package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.TransactionDAO;

import java.util.List;

/**
 * Created by Zahed on 10/21/2015.
 */
public class TransactionDAOImpl extends AbstractDAO implements TransactionDAO {
    @Override
    public List<Transaction> getTransactions(Integer accountNum) {
        return null;
    }
}
