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

    private static String userIdRegex = "[a-z0-9]{4,10}";

    private static String nameRegex = "[a-zA-Z ]{2,40}";

    private static String addressRegex = "[a-zA-Z0-9-, ]{2,100}";

    private static String phoneRegex = "[0-9]{10,12}";

    private static String ssnRegex = "[0-9]{10}";

    private Pattern userIdPattern = Pattern.compile(userIdRegex);

    private Pattern userFullNamePattern = Pattern.compile(nameRegex);

    private Pattern addressPattern = Pattern.compile(addressRegex);

    private SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Pattern phonePattern = Pattern.compile(phoneRegex);

    private Pattern ssnPattern = Pattern.compile(ssnRegex);

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
        if (!AppConstants.GENDERS.containsKey(user.getGender())) {
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
        // Check all the strings
    }
}
