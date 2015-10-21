package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.Account;
import edu.asu.securebanking.constants.AppConstants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

/**
 * Created by Vikranth on 10/20/2015.
 */
public class AccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz == Account.class) {
            return true;
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        double balance = 0;

        try {
            balance = Double.parseDouble(account.getBalanceString());
        } catch (Exception e) {
            errors.rejectValue("balanceString", "account.deposit.number.error", "");
            return;
        }

        // check for min and max balance
        if (balance < AppConstants.MIN_BALANCE
                || balance > AppConstants.MAX_BALANCE) {
            errors.rejectValue("balanceString", "account.deposit.minmax.error",
                    new Object[]{AppConstants.MIN_BALANCE, AppConstants.MAX_BALANCE},
                    "");
        }

        // Check if type is okay
        if (!AppConstants.ACCOUNT_TYPES.containsKey(
                account.getAccountType())) {
            errors.rejectValue("accountType", "account.type.error", "");
        }

        if (!errors.hasErrors())
            account.setBalance(new BigDecimal(balance));
    }
}
