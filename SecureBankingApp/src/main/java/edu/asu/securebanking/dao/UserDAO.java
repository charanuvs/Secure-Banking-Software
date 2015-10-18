package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.AppUser;

/**
 * Created by Vikranth on 10/12/2015.
 */
public interface UserDAO {

    /**
     * Get user for an email ID
     *
     * @param username
     * @return user
     */
    public AppUser getUser(final String username);

    /**
     * @param user
     * @return added
     */
    public void addUser(final AppUser user);
}
