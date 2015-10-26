package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.Log;
import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.LogDAO;

import java.util.List;

/**
 * Created by Zahed on 10/21/2015.
 */
public class LogDAOImpl extends AbstractDAO implements LogDAO {
    @Override
    public List<Log> getLogs(AppUser user) {
        return null;
    }
}
