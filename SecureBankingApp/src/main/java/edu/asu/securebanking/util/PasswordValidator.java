package edu.asu.securebanking.util;

import edu.asu.securebanking.beans.PasswordBean;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by Vikranth on 10/18/2015.
 */
public class PasswordValidator implements Validator {

    private static String allRegex = "^[0-9A-Za-z!@#\\$]{6,15}$";

    private static Pattern pwdPattern = Pattern.compile(allRegex);

    private static Pattern oneSpecialCharPattern = Pattern.compile("^.*[!@#\\$]+.*$");

    private static Pattern oneDigitPattern = Pattern.compile("^.*[0-9]+.*$");

    private static Pattern oneCharPattern = Pattern.compile("^.*[a-zA-Z]+.*$");

    @Override
    public boolean supports(Class<?> clazz) {
        if (clazz == PasswordBean.class) {
            return true;
        }
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordBean pwd = (PasswordBean) target;

        String password = pwd.getPassword();

        if (!(pwdPattern.matcher(password).matches()
                && oneSpecialCharPattern.matcher(password).matches()
                && oneCharPattern.matcher(password).matches()
                && oneDigitPattern.matcher(password).matches())) {
            errors.rejectValue("password", "pwd.error", "");
            return;
        }

        // password matched
        if (!password.equals(pwd.getConfirmPassword()))
            errors.rejectValue("confirmPassword", "pwd.samepwd.error", "");
    }
}
