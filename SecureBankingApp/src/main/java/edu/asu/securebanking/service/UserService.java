package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PasswordBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.UserDAO;
import edu.asu.securebanking.exceptions.AppBusinessException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vikranth on 10/12/2015.
 */
@Service
public class UserService {

    @Autowired
    @Qualifier("userDAO")
    private UserDAO userDAO;

    @Autowired
    @Qualifier("pwdEncoder")
    private PasswordEncoder encoder;

    @Autowired
    @Qualifier("messageSource")
    private ResourceBundleMessageSource messageSource;

    protected static Logger LOGGER = Logger.getLogger(UserService.class);

    /**
     * Get user for emailID
     *
     * @param username
     * @return user
     */
    @Transactional(rollbackOn = Throwable.class)
    public AppUser getUser(final String username) {

        return userDAO.getUser(username);
    }

    /**
     * @param user
     */
    @Transactional(rollbackOn = Throwable.class)
    public void addUser(final AppUser user) {
        // Hash the password of the user
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.addUser(user);
    }

    /**
     * @return externalUsers
     */
    @Transactional(rollbackOn = Throwable.class)
    public List<AppUser> getExternalUsers() {
        return userDAO.getExternalUsers();
    }

    /**
     * @param username
     * @return user
     */
    @Transactional(rollbackOn = Throwable.class)
    public AppUser getExternalUser(String username) {
        AppUser user = userDAO.getUser(username);

        // check if external user
        if (user != null &&
                AppConstants.EXTERNAL_USERS_ROLES.containsKey(user.getUserType()))
            return user;

        return null;
    }

    /**
     * Update user information
     *
     * @param user
     */
    @Transactional(rollbackOn = Throwable.class)
    public void updateUser(AppUser user) {
        userDAO.updateUser(user);
    }

    /**
     * @param username
     * @param pwd
     * @throws AppBusinessException
     */
    @Transactional(rollbackOn = Throwable.class)
    public AppUser getUserforPwd(String username, PasswordBean pwd)
            throws AppBusinessException {

        // User has to exist
        AppUser user = getUser(username);

        if (!encoder.matches(pwd.getCurrentPassword(), user.getPassword())) {
            throw new AppBusinessException(messageSource.getMessage("pwd.incorrectcurrentpwd",
                    new Object[]{},
                    Locale.getDefault()));
        }
        user.setPassword(encoder.encode(pwd.getPassword()));
        return user;
    }

}
