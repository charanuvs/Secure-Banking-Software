package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.beans.Account;

import java.util.List;

/**
 * Created by Zahed on 10/21/2015.
 */
public interface TransactionDAO {

    /**
     * Get transaction for a account
     *
     * @param accountNum
     * @return transactions
     */
    public List<Transaction> getTransactions(Integer accountNum);
}
