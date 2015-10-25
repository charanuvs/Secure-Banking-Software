package edu.asu.securebanking.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.dao.TransactionDAO;


public class TransactionService {
	
    @Autowired
    @Qualifier("transactionDAO")
    private TransactionDAO transactionDAO;
    
    /**
     * @param transaction
     */
    @Transactional(rollbackOn = Throwable.class)
    public void addTransaction(Transaction transaction) {
        // Hash the password of the user
        
        transactionDAO.addTransaction(transaction);
    }
}
