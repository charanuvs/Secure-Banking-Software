package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.TransactionDAO;


/**
 * Created by Rishabh on 10/25/2015.
 */

public class TransactionDAOImpl extends AbstractDAO implements TransactionDAO {

	@Override
    public void addTransaction(Transaction transaction) {
		getSession().save(transaction);
	}
}
