package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    @Transactional(rollbackOn = Throwable.class)
    public void updateUser(AppUser user) {
        userDAO.updateUser(user);
    }

}
