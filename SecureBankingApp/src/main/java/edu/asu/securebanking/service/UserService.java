package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * @param emailID
     * @return user
     */
    public AppUser getUser(final String username) {

        return userDAO.getUser(username);
    }

    public void addUser(final AppUser user) {
        // Hash the password of the user
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.addUser(user);
    }
}
