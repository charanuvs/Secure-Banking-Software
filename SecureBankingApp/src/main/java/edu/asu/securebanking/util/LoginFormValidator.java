package edu.asu.securebanking.util;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.securebanking.beans.LoginFormBean;

/**
 * @author Vikranth
 *
 */
public class LoginFormValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		if (clazz == LoginFormBean.class) {
			return true;
		}
		return false;
	}

	public void validate(Object obj	, Errors errors) {
		LoginFormBean bean = (LoginFormBean) obj;
		
		if (StringUtils.hasText(bean.getUsername())) {
			bean.setUsername(bean.getUsername().trim());
		} else {
			errors.reject("username", "Username is empty");
		}
		
		if (!StringUtils.hasText(bean.getPassword())) {
			errors.reject("password", "Password is empty");
		}
	}

}
