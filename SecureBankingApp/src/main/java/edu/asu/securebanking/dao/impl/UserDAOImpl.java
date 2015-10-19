package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.UserDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Vikranth on 10/12/2015.
 */
@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {
    @Override
    @Transactional
    public List<AppUser> getExteranlUsers() {
        Criteria criteria = getSession().createCriteria(AppUser.class);
        criteria.add(Restrictions.in("userType",
                AppConstants.EXTERNAL_USERS_ROLES.keySet()));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    @Override
    @Transactional
    public List<AppUser> getInternalUsers() {
        return null;
    }

    @Override
    @Transactional
    public void addUser(AppUser user) {
        getSession().save(user);
    }

    @Override
    @Transactional
    public AppUser getUser(String username) {
        /*Criteria criteria = getSession().createCriteria(AppUser.class);
        criteria.add(Restrictions.eq("userId", username));
        return (AppUser) criteria.uniqueResult();*/
        return (AppUser) getSession().get(AppUser.class, username);
    }

}
