package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.dao.TransactionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.List;


public class TransactionService {

    @Autowired
    @Qualifier("transactionDAO")
    private TransactionDAO transactionDAO;

    /**
     * @param transaction
     */
    @Transactional(rollbackOn = Throwable.class)
    public void addTransaction(Transaction transaction) {

        transactionDAO.addTransaction(transaction);
    }


    /**
     * @return PendingNonCriticalTransactions
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<Transaction> getPendingNonCriticalTransactions() {
        return transactionDAO.getPendingNonCriticalTransactions();
    }

    /**
     * @return PendingCriticalTransactions
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<Transaction> getPendingCriticalTransactions() {
        return transactionDAO.getPendingCriticalTransactions();
    }


    /**
     * @param transactionId
     * @return transaction
     */
    @Transactional(rollbackOn = Throwable.class)
    public Transaction getTransaction(String transactionId) {
        return transactionDAO.getTransaction(transactionId);

    }


    /**
     * Update transaction information
     *
     * @param t
     */
    @Transactional(rollbackOn = Throwable.class)
    public void updateTransaction(Transaction t) {
        transactionDAO.updateTransaction(t);
    }

    /**
     * delete a transaction
     *
     * @param t
     */
    @Transactional(rollbackOn = Throwable.class)
    public void deleteTransaction(Transaction t) {
        transactionDAO.deleteTransaction(t);
    }
}
