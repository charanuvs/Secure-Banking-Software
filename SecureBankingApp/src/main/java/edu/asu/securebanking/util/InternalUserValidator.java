package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import org.springframework.validation.Errors;

/**
 * Created by Vikranth on 10/18/2015.
 */
public class InternalUserValidator extends AppUserValidator {

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        AppUser user = (AppUser) target;

        // Check for internal User roles
        if (!AppConstants.INTERNAL_USERS_ROLES.containsKey(user.getUserType())) {
            errors.rejectValue("userType", "userType", "Invalid User type");
        }

        // Check for Trans auth validator
        // set no
        user.setTransAuth(AppConstants.TRANSACTION_AUTHORIZED_NO);
    }
}
