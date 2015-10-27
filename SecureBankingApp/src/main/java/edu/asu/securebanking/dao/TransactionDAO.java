package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Transaction;

import java.util.List;

/**
 * Created by Zahed on 10/21/2015.
 */
public interface TransactionDAO {

    /**
     * @param transaction
     */
    public void addTransaction(Transaction transaction);

    /**
     * Get transaction for a account
     *
     * @param accountNum
     * @return transactions
     */
    public List<Transaction> getTransactions(Integer accountNum);

}
