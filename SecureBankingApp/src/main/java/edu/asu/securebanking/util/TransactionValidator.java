package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.Transaction;
import edu.asu.securebanking.constants.AppConstants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Created by Vikranth on 10/29/2015.
 */
public class TransactionValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz == Transaction.class) {
            return true;
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Transaction transaction = (Transaction) target;

        // check fields
        if (!isValidAccountNum(transaction.getToAccountNumber())
                || !isValidAccountNum(transaction.getFromAccountNumber())) {
            errors.rejectValue("fromAccountNumber", "", "Invalid account numbers");
        } else {
            transaction.setFromAccountNumberInteger(Integer.parseInt(transaction.getFromAccountNumber()));
            transaction.setToAccountNumberInteger(Integer.parseInt(transaction.getToAccountNumber()));
        }

        if (!isValidAmount(transaction.getAmountString())) {
            errors.rejectValue("amountString", "", "Invalid amount. Min more than 0 and max is less than 1 million");
        } else {
            transaction.setAmount(new BigDecimal(transaction.getAmountString()));
        }
    }

    public static boolean isValidAmount(String str) {
        try {
            double d = Double.parseDouble(str);
            if (d <= 0 || d > AppConstants.ONE_MILLION)
                return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidAccountNum(String str) {
        try {
            Integer d = Integer.parseInt(str);
            if (d <= 0)
                return false;

        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
