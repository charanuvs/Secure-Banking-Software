package edu.asu.securebanking.dao.impl;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.dao.AbstractDAO;
import edu.asu.securebanking.dao.UserDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by Vikranth on 10/12/2015.
 */
@Repository
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    @Override
    @Transactional
    public AppUser getUser(String emailID) {
        Criteria criteria = getSession().createCriteria(AppUser.class);
        criteria.add(Restrictions.eq("email", emailID));
        return (AppUser) criteria.uniqueResult();
    }
}
