package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.UserDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vikranth on 10/12/2015.
 */
@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    @Override
    public void updateUser(AppUser user) {
        getSession().saveOrUpdate(user);
    }

    @Override
    public List<AppUser> getExternalUsers() {
        Criteria criteria = getSession().createCriteria(AppUser.class);
        criteria.add(Restrictions.in("userType",
                AppConstants.EXTERNAL_USERS_ROLES.keySet()));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    @Override
    public List<AppUser> getInternalUsers() {
        return null;
    }

    @Override
    public void addUser(AppUser user) {
        getSession().save(user);
    }

    @Override
    public AppUser getUser(String username) {

        return (AppUser) getSession().get(AppUser.class, username);
    }

}
