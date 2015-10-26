package edu.asu.securebanking.dao;

import edu.asu.securebanking.beans.Log;
import edu.asu.securebanking.beans.AppUser;

import java.util.List;

/**
 * Created by Zahed on 10/21/2015.
 */
public interface LogDAO {

    /**
     * Get Logs for a User
     *
     * @param user
     * @return logs
     */
    public List<Log> getLogs(AppUser user);
}
