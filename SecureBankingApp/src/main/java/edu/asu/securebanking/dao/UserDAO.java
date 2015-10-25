package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.AppUser;

import java.util.List;

/**
 * Created by Vikranth on 10/12/2015.
 */
public interface UserDAO {

    /**
     * Get user for an username
     *
     * @param username
     * @return user
     */
    public AppUser getUser(final String username);

    /**
     * Update user
     *
     * @param user
     */
    public void updateUser(AppUser user);

    /**
     * @param user
     * @return added
     */
    public void addUser(final AppUser user);

    /**
     * @return externalUsers
     */
    public List<AppUser> getExternalUsers();

    /**
     * @return employees
     */
    public List<AppUser> getInternalUsers();

}
