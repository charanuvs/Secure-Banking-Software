package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Transaction;

public interface TransactionDAO {

    /**
     * @param Transaction
     * @return added
     */
    public void addTransaction(Transaction transaction);

}
