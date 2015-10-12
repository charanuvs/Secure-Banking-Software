package edu.asu.securebanking.service;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by Vikranth on 10/12/2015.
 */
@Service
public class UserService {

    @Autowired
    @Qualifier("userDAO")
    UserDAO userDAO;

    /**
     * Get user for emailID
     *
     * @param emailID
     * @return user
     */
    public AppUser getUser(final String emailID) {

        return userDAO.getUser("vdoosa@asu.edu");
    }
}
