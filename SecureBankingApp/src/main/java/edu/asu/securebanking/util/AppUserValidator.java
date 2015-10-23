package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Vikranth on 10/18/2015.
 */
public class AppUserValidator implements Validator {

    protected static String defaultMerchantDate = "1980-01-01";

    protected static String userIdRegex = "^[a-z0-9]{4,10}$";

    protected static String nameRegex = "^[a-zA-Z ]{2,40}$";

    protected static String addressRegex = "^[a-zA-Z0-9-, ]{2,100}$";

    protected static String phoneRegex = "^[0-9]{10,12}$";

    protected static String ssnRegex = "^[0-9]{10}$";

    protected static String simpleEmailRegex = "^\\S+@\\S+$";

    protected static Pattern userIdPattern = Pattern.compile(userIdRegex);

    protected static Pattern userFullNamePattern = Pattern.compile(nameRegex);

    protected static Pattern addressPattern = Pattern.compile(addressRegex);

    protected static SimpleDateFormat dobFormat = AppConstants.DATE_FORMAT;

    protected static Pattern phonePattern = Pattern.compile(phoneRegex);

    protected static Pattern ssnPattern = Pattern.compile(ssnRegex);

    protected static Pattern emailPattern = Pattern.compile(simpleEmailRegex);

    protected static String pwdRegex = "^[0-9]$";

    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz == AppUser.class) {
            return true;
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AppUser user = (AppUser) target;

        // user id
        if (!userIdPattern.matcher(user.getUserId()).matches()) {
            errors.rejectValue("userId", "userId",
                    "Invalid username/Characters plus Numeric, min 4 and max 10 characters");
        }

        // name
        if (StringUtils.hasText(user.getName())) {
            user.setName(user.getName().trim().replaceAll("\\s", " "));
            if (!userFullNamePattern.matcher(user.getName()).matches()) {
                errors.rejectValue("name", "name", "Invalid name");
            }
        } else {
            errors.rejectValue("name", "name", "Invalid name");
        }

        // address
        if (StringUtils.hasText(user.getAddress())) {
            user.setAddress(user.getAddress().replaceAll("\\s", " "));
            if (!addressPattern.matcher(user.getAddress()).matches())
                errors.rejectValue("address", "address", "Invalid address");
        } else {
            errors.rejectValue("address", "address", "Invalid address");
        }

        // Date of birth

        try {
            // If he is a merchant/organization set the default date
            if (AppConstants.ROLE_MERCHANT
                    .equalsIgnoreCase(user.getUserType())) {
                // Default date 1980-01-01
                user.setDateString(defaultMerchantDate);
            }

            Date date = dobFormat.parse(user.getDateString());
            user.setDob(date);
            Date currentDate = new Date();
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);

            Calendar dobCal = Calendar.getInstance();
            dobCal.setTime(user.getDob());


            if (!(currentCal.get(Calendar.YEAR)
                    - dobCal.get(Calendar.YEAR) >= AppConstants.MIN_AGE)) {
                errors.rejectValue("dob", "dob", "Invalid date of birth. Min age is 15");
            }
        } catch (Exception e) {
            errors.rejectValue("dob", "dob", "Invalid date of birth field");
        }

        // Phone
        if (!phonePattern.matcher(user.getPhoneNumber()).matches()) {
            errors.rejectValue("phoneNumber", "phoneNumber", "Invalid Phone");
        }

        // Gender
        if (AppConstants.ROLE_MERCHANT
                .equalsIgnoreCase(user.getUserType())) {
            user.setGender(AppConstants.GENDER_OTHER);
        } else if (!AppConstants.GENDERS.containsKey(user.getGender())) {
            errors.rejectValue("gender", "gender", "Invalid Gender");
        }

        // SSN
        if (StringUtils.hasText(user.getSsn())) {
            if (!ssnPattern.matcher(user.getSsn()).matches())
                errors.rejectValue("ssn", "ssn", "Invalid SSN, " +
                        "SSN should either be empty or 10 digit number");
        } else {
            user.setSsn("");
        }

        if (AppConstants.ROLE_MERCHANT
                .equalsIgnoreCase(user.getUserType())) {
            user.setSsn("");
            user.setGender(AppConstants.GENDER_OTHER);
        }

        // Check all the strings
        if (StringUtils.hasText(user.getEmail())) {
            user.setEmail(user.getEmail().trim());
            if (!emailPattern.matcher(user.getEmail()).matches())
                errors.rejectValue("email", "email", "Email is invalid");
            else if (user.getEmail().length() > AppConstants.MAX_EMAIL_LEN)
                errors.rejectValue("email", "email", "Email is invalid");
        } else {
            errors.rejectValue("email", "email", "Invalid email");
        }

        // Status
        if (!AppConstants.USER_STATUS.containsKey(user.getStatus())) {
            errors.rejectValue("status", "status", "Invalid status");
        }
    }
}
