package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.AccountDAO;
import edu.asu.securebanking.dao.TransactionDAO;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


public class TransactionService {

    @Autowired
    @Qualifier("accountDAO")
    private AccountDAO accountDAO;

    @Autowired
    @Qualifier("transactionDAO")
    private TransactionDAO transactionDAO;

    private static Logger LOGGER = Logger.getLogger(TransactionDAO.class);

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

    @Transactional(rollbackOn = Throwable.class)
    public void validateTransaction(Transaction transaction, String username) throws AppBusinessException {

        // transaction validated
        Account fromAccount = accountDAO.getAccount(transaction.getFromAccountNumberInteger());

        if (fromAccount == null || !fromAccount.getUser().getUserId().equals(username)) {
            throw new AppBusinessException("Your transaction was not processed. \n" +
                    "You must be the owner of the source acount.");
        }

        Account toAccount = accountDAO.getAccount(transaction.getToAccountNumberInteger());

        if ("TRANSFER".equals(transaction.getTransactionType())) {
            if (toAccount == null || !toAccount.getUser().getUserType().equals(AppConstants.ROLE_NORMAL)) {
                throw new AppBusinessException("Your transaction was not processed. \n" +
                        "Invalid To account number.");
            }

        } else {
            if (toAccount == null || !toAccount.getUser().getUserType().equals(AppConstants.ROLE_MERCHANT)) {
                throw new AppBusinessException("Your transaction was not processed. \n" +
                        "Invalid Payment account.");
            }
        }

        if (fromAccount.getAccountNum().equals(toAccount.getAccountNum())) {
            throw new AppBusinessException("Your transaction was not processed. \n" +
                    "Cannot transfer funds to the same account.");
        }

        LOGGER.info(fromAccount + "\n" + transaction);

        if (fromAccount.getBalance().doubleValue() < transaction.getAmount().doubleValue()) {
            throw new AppBusinessException("Your transaction was not processed. \n" +
                    "Insufficent funds.");
        }

        transaction.setToAccount(toAccount);
        transaction.setFromAccount(fromAccount);
        transaction.setDate(new Date());
    }

    /**
     * @param transaction
     */
    @Transactional(rollbackOn = Throwable.class)
    public void addNewTransaction(Transaction transaction, String username) throws AppBusinessException {
        validateTransaction(transaction, username);
        transactionDAO.addTransaction(transaction);
    }
}
